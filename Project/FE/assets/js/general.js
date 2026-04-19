// general.js (revised for page-based Register + OTP)
// ===================== Global helpers =====================
"use strict";

import { initCartSlide } from "./cart.js";
import { initApp } from "./fetchComponent/initApp.js";
import { loadLayout } from "./fetchComponent/loadLayout.js";

// ---- API base ----
const API_BASE = "http://127.0.0.1:8080";

// Chuẩn hoá fetch: auto include cookie + no-store để tránh cache
async function apiFetch(path, opts = {}) {
  const resp = await fetch(
    path?.startsWith?.("http") ? path : `${API_BASE}${path}`,
    {
      credentials: "include",
      cache: "no-store",
      ...opts,
      headers: {
        ...(opts.headers || {}),
      },
    }
  );
  return resp;
}

function getUser() {
  try { return JSON.parse(sessionStorage.getItem("user")); } catch { return null; }
}
function setUser(u) { sessionStorage.setItem("user", JSON.stringify(u)); }
function clearUser() { sessionStorage.clear(); }

// Small utils
const qs = (sel, root=document) => root.querySelector(sel);
const qsa = (sel, root=document) => Array.from(root.querySelectorAll(sel));
const byId = (id) => document.getElementById(id);

// ===================== Auth Popup (Login only; Signup -> page) =====================
function initAuthPopup() {
  const popup = byId("auth-popup");                    // modal wrapper
  const userAccountBtn = qs(".user-account");          // button in header
  const overlay = qs(".blur-bg-overlay");              // overlay
  if (!popup || !userAccountBtn) return;

  // Nếu đã login thì không gắn popup nữa
  if (getUser()) return;

  const closeBtn = popup.querySelector(".close-btn");

  // Mở popup (mặc định tab login). Ta KHÔNG dùng tab signup nữa.
  userAccountBtn.addEventListener("click", (e) => {
    e.preventDefault();
    document.body.classList.add("show-popup");
    popup.classList.remove("show-signup");
  });

  // Đóng popup
  closeBtn?.addEventListener("click", () => {
    document.body.classList.remove("show-popup");
    popup.classList.remove("show-signup");
  });
  overlay?.addEventListener("click", () => {
    document.body.classList.remove("show-popup");
    popup.classList.remove("show-signup");
  });

  // Đổi hành vi nút "Tạo tài khoản" trong popup -> sang trang register.html
  const signupLink = byId("signup-link");
  if (signupLink) {
    signupLink.addEventListener("click", (e) => {
      e.preventDefault();
      document.body.classList.remove("show-popup");
      popup.classList.remove("show-signup");
      // Điều hướng sang trang đăng ký thực sự
      window.location.href = "/register.html";
    });
  }

  // Nếu popup đang mở mà auth thay đổi (login xong) -> tự đóng
  window.addEventListener("authChanged", () => {
    document.body.classList.remove("show-popup");
    popup.classList.remove("show-signup");
  });
}

// ===================== Login =====================
function initLoginForm() {
  const loginForm = qs(".form-box.login form");
  if (!loginForm) return;

  const usernameInput = loginForm.querySelector('input[type="text"], input[name="username"]');
  const passwordInput = loginForm.querySelector('input[type="password"], input[name="password"]');

  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = usernameInput?.value?.trim() ?? "";
    const password = passwordInput?.value ?? "";

    if (!username || !password) {
      alert("Vui lòng nhập đủ tên đăng nhập và mật khẩu.");
      return;
    }

    try {
      const res = await apiFetch("/login", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`,
      });

      if (!res.ok) {
        alert("Sai tài khoản hoặc mật khẩu!");
        return;
      }

      // Lấy info user sau khi login
      const userRes = await apiFetch("/auth/me");
      if (!userRes.ok) {
        alert("Không lấy được thông tin người dùng!");
        return;
      }
      const user = await userRes.json();
      setUser(user);

      // Đóng popup + phát sự kiện cho các module khác (cart, header…)
      window.dispatchEvent(new Event("authChanged"));
    } catch (err) {
      console.error("Login error:", err);
      alert("Lỗi kết nối đến server.");
    }
  });
}

// ===================== Register (PAGE) -> redirect to OTP PAGE =====================
function initRegisterForm() {
  const form = byId("register-form");
  if (!form) return;

  // --- Xoá mọi thuộc tính có thể kích hoạt popup/modal từ HTML cũ ---
  const registerBtn = byId("registerBtn") || form.querySelector('button[type="submit"]');
  [registerBtn, form].forEach((el) => {
    if (!el) return;
    ["data-bs-toggle","data-toggle","data-target","data-bs-target","onclick"]
      .forEach(a => el.hasAttribute?.(a) && el.removeAttribute(a));
  });

  // ---- Avatar preview ----
  const avatarFileInput = byId("avatarFile");
  const avatarUrlInput  = byId("avatarUrl");
  const avatarPreview   = byId("avatarPreview");
  const clearAvatarBtn  = byId("clearAvatarBtn");

  avatarFileInput?.addEventListener("change", () => {
    const file = avatarFileInput.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = (e) => {
      if (avatarPreview) {
        avatarPreview.src = e.target.result;
        avatarPreview.style.display = "block";
      }
      if (clearAvatarBtn) clearAvatarBtn.style.display = "inline-block";
    };
    reader.readAsDataURL(file);
  });

  avatarUrlInput?.addEventListener("input", () => {
    const url = avatarUrlInput.value.trim();
    if (!avatarPreview || !clearAvatarBtn) return;
    if (url.startsWith("http")) {
      avatarPreview.src = url;
      avatarPreview.style.display = "block";
      clearAvatarBtn.style.display = "inline-block";
    } else {
      avatarPreview.src = "";
      avatarPreview.style.display = "none";
      clearAvatarBtn.style.display = "none";
    }
  });

  clearAvatarBtn?.addEventListener("click", () => {
    if (avatarPreview) { avatarPreview.src = ""; avatarPreview.style.display = "none"; }
    if (avatarFileInput) avatarFileInput.value = "";
    if (avatarUrlInput)  avatarUrlInput.value  = "";
    clearAvatarBtn.style.display = "none";
  });

  // ---- Helpers ----
  const toBase64 = (file) => new Promise((resolve, reject) => {
    const r = new FileReader(); r.onload = () => resolve(r.result); r.onerror = reject; r.readAsDataURL(file);
  });
  const resultEl = byId("register-result");
  const showMsg = (ok, msg) => {
    if (!resultEl) return;
    resultEl.className = ok ? "text-success" : "text-danger";
    resultEl.textContent = msg || "";
  };
  const redirectToOtp = (email) => {
    try { sessionStorage.setItem("pendingEmail", email); } catch(e) {}
    const url = `/otp.html?email=${encodeURIComponent(email)}`;
    window.location.replace(url);
    setTimeout(() => { if (!/otp\.html/i.test(location.href)) window.location.href = url; }, 120);
  };

  // ---- Submit Register -> redirect to OTP PAGE (không popup) ----
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.stopImmediatePropagation) e.stopImmediatePropagation();

    const username = form.username?.value?.trim();
    const password = form.password?.value;
    const email    = form.email?.value?.trim();
    if (!username || !password || !email) {
      showMsg(false, "Vui lòng điền đủ Tên đăng nhập, Mật khẩu và Email.");
      return;
    }

    // optional avatar
    let avatar = "";
    const file = form.avatarFile?.files?.[0];
    const url  = form.avatarUrl?.value?.trim?.();
    if (file) avatar = await toBase64(file);
    else if (url) avatar = url;

    const payload = {
      username,
      password,
      name: form.name?.value?.trim() || "",
      email,
      phone: form.phone?.value?.trim() || "",
      address: form.address?.value?.trim() || "",
      avatar
      // KHÔNG gửi isActive từ client
    };

    // UI busy
    registerBtn?.setAttribute("disabled","true");
    const oldText = registerBtn?.innerHTML;
    if (registerBtn) registerBtn.innerHTML = "Đang gửi…";

    try {
      const res = await apiFetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      // coi mọi 2xx là OK
      if (res.status >= 200 && res.status < 300) {
        redirectToOtp(email);
        return;
      }
      const text = await res.text().catch(()=>"");
      showMsg(false, text || "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin.");
    } catch (err) {
      console.error("Register error:", err);
      showMsg(false, "Không thể kết nối máy chủ (CORS/Network).");
    } finally {
      if (registerBtn) {
        registerBtn.removeAttribute("disabled");
        registerBtn.innerHTML = oldText || "Đăng ký";
      }
    }
  }, true); // useCapture để handler này chạy trước các listener cũ
}

// ===================== OTP PAGE (riêng một trang) =====================
function initOtpPage() {
  const otpForm = byId("otp-form");
  if (!otpForm) return; // Không ở trang OTP thì bỏ qua

  const emailInput = byId("otpEmail") || byId("email") || otpForm.querySelector('input[name="email"]');
  const codeInput  = byId("otpCode")  || byId("code")  || otpForm.querySelector('input[name="code"]');
  const verifyBtn  = byId("otpVerifyBtn") || byId("verifyBtn") || otpForm.querySelector('button[type="submit"]');
  const emailDisplay = byId("otp-email-display") || byId("emailDisplay");
  const resendBtn = byId("resendOtpBtn") || byId("resendBtn");
  const otpTimer  = byId("otp-timer") || byId("countdownNote");
  const otpResult = byId("otp-result") || byId("otpResult");

  const showMsg = (ok, msg) => {
    if (!otpResult) return;
    otpResult.className = ok ? "text-success" : "text-danger";
    otpResult.textContent = msg || "";
  };

  // Lấy email từ query string hoặc sessionStorage
  const url = new URL(location.href);
  const emailFromQuery = url.searchParams.get("email");
  const email = emailFromQuery || sessionStorage.getItem("pendingEmail") || "";
  if (!email) {
    // Không có email ngữ cảnh -> quay về đăng ký
    location.replace("/register.html");
    return;
  }
  if (emailInput) emailInput.value = email;
  if (emailDisplay) emailDisplay.textContent = email;

  // Cooldown resend
  let intervalId = null;
  const startCooldown = (s) => {
    if (!resendBtn) return;
    clearInterval(intervalId);
    let remain = s;
    resendBtn.disabled = true;
    if (resendBtn) resendBtn.textContent = `Gửi lại mã (${remain}s)`;
    if (otpTimer) otpTimer.textContent = "Bạn có thể yêu cầu mã mới sau khi hết thời gian đếm ngược.";
    intervalId = setInterval(() => {
      remain--;
      if (resendBtn) resendBtn.textContent = `Gửi lại mã (${remain}s)`;
      if (remain <= 0) {
        clearInterval(intervalId);
        resendBtn.disabled = false;
        if (resendBtn) resendBtn.textContent = "Gửi lại mã";
        if (otpTimer) otpTimer.textContent = "";
      }
    }, 1000);
  };
  startCooldown(60);

  // Verify OTP
  otpForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const code = codeInput?.value?.trim();
    if (!/^\d{6}$/.test(code || "")) {
      showMsg(false, "Mã OTP phải gồm 6 chữ số.");
      return;
    }
    const old = verifyBtn?.innerHTML;
    verifyBtn?.setAttribute("disabled","true");
    if (verifyBtn) verifyBtn.innerHTML = "Đang xác minh…";

    try {
      const res = await apiFetch("/auth/verify-otp", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, code }),
      });
      const text = await res.text().catch(()=> "");

      if (res.ok) {
        showMsg(true, "Xác thực email thành công. Đăng ký hoàn tất!");
        sessionStorage.removeItem("pendingEmail");
        // Tùy chọn: chuyển sang trang đăng nhập
        // setTimeout(() => location.href = "/login.html", 1000);
      } else {
        showMsg(false, text || "Mã OTP không đúng hoặc đã hết hạn.");
      }
    } catch (err) {
      console.error("verify-otp error:", err);
      showMsg(false, "Không thể xác thực lúc này.");
    } finally {
      verifyBtn?.removeAttribute("disabled");
      if (verifyBtn) verifyBtn.innerHTML = old || "Xác minh OTP";
    }
  });

  // Resend OTP
  resendBtn?.addEventListener("click", async () => {
    resendBtn.disabled = true;
    try {
      const res = await apiFetch("/auth/otp/resend", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });
      if (res.ok) {
        showMsg(true, "Đã gửi lại mã OTP.");
        startCooldown(60);
      } else {
        const text = await res.text().catch(()=> "");
        showMsg(false, text || "Không thể gửi lại OTP lúc này.");
        startCooldown(10);
      }
    } catch (err) {
      console.error("resend-otp error:", err);
      showMsg(false, "Không thể gửi lại OTP lúc này.");
      startCooldown(10);
    }
  });
}

// ===================== Header sau khi đăng nhập (avatar + logout) =====================
function renderUserHeader() {
  const user = getUser();
  const userAccount = qs(".user-account");
  if (!userAccount) return;

  // Nếu chưa login → giữ nút “Tài khoản”
  if (!user) return;

  const isAdmin = user.role === "ROLE_ADMIN";

  userAccount.innerHTML = `
    <div class="user-avatar-wrap" style="display:inline-flex;align-items:center;gap:8px;cursor:pointer;position:relative;vertical-align:middle;">
      <img src="${user.avatar || "/default-avatar.png"}" alt="avatar" style="width:30px;height:30px;border-radius:50%;object-fit:cover;">
      <span style="font-size:14px;">${user.name ?? user.username ?? user.email ?? "Tài khoản"}</span>
      <div class="user-dropdown" style="
        display:none;
        position:absolute;
        right:0;
        top:120%;
        background:#fff;
        border:1px solid #ddd;
        padding:8px 0;
        border-radius:6px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        z-index:1000;
        min-width:180px;
      ">
        <a href="/profile.html" class="dropdown-item" style="display:flex;align-items:center;gap:8px;padding:8px 16px;color:#333;text-decoration:none;font-size:14px;">
          <i class="fa-solid fa-user-tie" style="font-size:16px;"></i>Thông tin tài khoản
        </a>
        ${isAdmin ? `
        <a href="/admin.html" class="dropdown-item" style="display:flex;align-items:center;gap:8px;padding:8px 16px;color:#333;text-decoration:none;font-size:14px;">
          <i class="fa-brands fa-windows" style="font-size:16px;"></i> Quản lý
        </a>` : ""}
        <a href="#" id="logoutBtn" class="dropdown-item" style="display:flex;align-items:center;gap:8px;padding:8px 16px;color:#333;text-decoration:none;font-size:14px;">
          <i class="fa-solid fa-arrow-right-from-bracket" style="font-size:16px;"></i> Đăng xuất
        </a>
      </div>
    </div>
  `;

  // Toggle dropdown
  const avatarWrap = userAccount.querySelector(".user-avatar-wrap");
  const dropdown = userAccount.querySelector(".user-dropdown");

  avatarWrap?.addEventListener("click", (e) => {
    e.stopPropagation();
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
  });

  document.addEventListener("click", (e) => {
    if (!avatarWrap.contains(e.target)) dropdown.style.display = "none";
  });

  // Logout
  byId("logoutBtn")?.addEventListener("click", async (e) => {
    e.preventDefault();
    try { await apiFetch("/logout", { method: "POST" }); } catch (err) {
      console.warn("Logout request failed (ignored):", err);
    } finally {
      clearUser();
      window.dispatchEvent(new Event("authChanged"));
      window.location.reload();
    }
  });
}

// Khi auth thay đổi (login/logout) → vẽ lại header
window.addEventListener("authChanged", renderUserHeader);

// ===================== Init all =====================
document.addEventListener("DOMContentLoaded", async () => {
  await loadLayout();     // DOM layout (header/footer/popup)
  initApp();              // App init khác của bạn
  initCartSlide();        // Giỏ hàng
  initAuthPopup();        // Popup login (signup -> page)
  initRegisterForm();     // Form đăng ký (trang register.html) -> redirect OTP
  initLoginForm();        // Form login (trong popup)
  initOtpPage();          // Trang otp.html
  renderUserHeader();     // Render header theo trạng thái hiện tại
});
