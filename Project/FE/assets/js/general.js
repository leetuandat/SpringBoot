import { initCartSlide } from "./cart.js";
import { initApp } from "./fetchComponent/initApp.js";
import { loadLayout } from "./fetchComponent/loadLayout.js";

// Xử lý mở/đóng popup login + chuyển tab
function initAuthPopup() {
  const popup = document.getElementById("auth-popup");
  const userAccountBtn = document.querySelector(".user-account");

  const user = JSON.parse(sessionStorage.getItem("user"));
  if (user || !popup || !userAccountBtn) return; // 👈 thêm dòng này để bỏ qua nếu đã login

  const loginBox = popup.querySelector(".login");
  const signupBox = popup.querySelector(".signup");
  const closeBtn = popup.querySelector(".close-btn");
  const overlay = document.querySelector(".blur-bg-overlay");

  // Mở popup login
  userAccountBtn.addEventListener("click", (e) => {
    e.preventDefault();
    document.body.classList.add("show-popup");
    popup.classList.remove("show-signup");
  });

  // Đóng popup khi bấm nút close
  closeBtn?.addEventListener("click", () => {
    document.body.classList.remove("show-popup");
    popup.classList.remove("show-signup");
  });

  // Đóng popup khi bấm ra overlay nền
  overlay?.addEventListener("click", () => {
    document.body.classList.remove("show-popup");
    popup.classList.remove("show-signup");
  });

  // Chuyển qua form signup
  document.getElementById("signup-link")?.addEventListener("click", (e) => {
    e.preventDefault();
    popup.classList.add("show-signup");
  });

  // Quay lại login
  document.getElementById("login-link")?.addEventListener("click", (e) => {
    e.preventDefault();
    popup.classList.remove("show-signup");
  });
}

function initLoginForm() {
  const loginForm = document.querySelector(".form-box.login form");

  if (!loginForm) return;

  loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const username = loginForm.querySelector('input[type="text"]').value;
    const password = loginForm.querySelector('input[type="password"]').value;

    try {
      const res = await fetch("http://127.0.0.1:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        credentials: "include", // QUAN TRỌNG: để nhận session
        body: `username=${encodeURIComponent(
          username
        )}&password=${encodeURIComponent(password)}`,
      });

      if (!res.ok) {
        alert("Sai tài khoản hoặc mật khẩu!");
        return;
      }

      // GỌI API /auth/me sau login để lấy thông tin user
      const userRes = await fetch("http://127.0.0.1:8080/auth/me", {
        credentials: "include",
      });

      if (!userRes.ok) {
        alert("Không lấy được thông tin người dùng!");
        return;
      }

      const user = await userRes.json();
      sessionStorage.setItem("user", JSON.stringify(user));

      // ✅ Tự redirect sau khi login thành công
      document.body.classList.remove("show-popup");

      window.location.href = "/index.html"; // hoặc /home.html nếu bạn thích
    } catch (err) {
      console.error("Lỗi khi gửi yêu cầu:", err);
      alert("Lỗi kết nối đến server.");
    }
  });
}

// Xử lý logic form đăng ký tài khoản
function initRegisterForm() {
  const avatarFileInput = document.getElementById("avatarFile");
  const avatarUrlInput = document.getElementById("avatarUrl");
  const avatarPreview = document.getElementById("avatarPreview");
  const clearAvatarBtn = document.getElementById("clearAvatarBtn");

  avatarFileInput?.addEventListener("change", function () {
    const file = avatarFileInput.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        avatarPreview.src = e.target.result;
        avatarPreview.style.display = "block";
        clearAvatarBtn.style.display = "inline-block";
      };
      reader.readAsDataURL(file);
    }
  });

  avatarUrlInput?.addEventListener("input", function () {
    const url = avatarUrlInput.value.trim();
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

  clearAvatarBtn?.addEventListener("click", function () {
    avatarPreview.src = "";
    avatarPreview.style.display = "none";
    clearAvatarBtn.style.display = "none";
    avatarFileInput.value = "";
    avatarUrlInput.value = "";
  });

  document
    .getElementById("register-form")
    ?.addEventListener("submit", async function (e) {
      e.preventDefault();
      const form = e.target;
      const fileInput = form.avatarFile.files[0];
      const avatarUrl = form.avatarUrl.value.trim();

      const toBase64 = (file) =>
        new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = () => resolve(reader.result);
          reader.onerror = (error) => reject(error);
        });

      let avatar = "";
      if (fileInput) {
        avatar = await toBase64(fileInput);
      } else if (avatarUrl !== "") {
        avatar = avatarUrl;
      }

      const data = {
        username: form.username.value,
        password: form.password.value,
        name: form.name.value,
        email: form.email.value,
        phone: form.phone.value,
        address: form.address.value,
        avatar: avatar,
        isActive: true,
      };

      const res = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      const result = await res.text();
      document.getElementById("register-result").textContent = result;
    });
}

function renderUserHeader() {
  const user = JSON.parse(sessionStorage.getItem("user"));
  const userAccount = document.querySelector(".user-account");

  if (!user || !userAccount) return;

  // Thay thế nội dung bên trong nút "user-account" bằng avatar + tên + dropdown
  userAccount.innerHTML = `
  <div class="user-avatar-wrap" style="display:inline-flex;align-items:center;gap:8px;cursor:pointer;position:relative;vertical-align:middle;">
    <img src="${user.avatar}" alt="avatar" style="width:30px;height:30px;border-radius:50%;object-fit:cover;">
    <span style="font-size:14px;">${user.name}</span>
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
      <a href="/profile.html" class="dropdown-item" style="
        display:flex;
        align-items:center;
        gap:8px;
        padding:8px 16px;
        color:#333;
        text-decoration:none;
        font-size:14px;
      ">
        <i class="icon icon-user" style="font-size:16px;"></i> Thông tin tài khoản
      </a>
      <a href="#" id="logoutBtn" class="dropdown-item" style="
        display:flex;
        align-items:center;
        gap:8px;
        padding:8px 16px;
        color:#333;
        text-decoration:none;
        font-size:14px;
      ">
        <i class="icon icon-exit" style="font-size:16px;"></i> Đăng xuất
      </a>
    </div>
  </div>
`;

  // Gắn sự kiện toggle dropdown
  const avatarWrap = userAccount.querySelector(".user-avatar-wrap");
  const dropdown = userAccount.querySelector(".user-dropdown");

  avatarWrap?.addEventListener("click", (e) => {
    e.stopPropagation();
    dropdown.style.display =
      dropdown.style.display === "block" ? "none" : "block";
  });

  // Đóng dropdown khi click ra ngoài
  document.addEventListener("click", (e) => {
    if (!avatarWrap.contains(e.target)) {
      dropdown.style.display = "none";
    }
  });

  // Xử lý logout
  document.getElementById("logoutBtn")?.addEventListener("click", async () => {
    await fetch("/logout", { method: "POST" });
    sessionStorage.clear();
    window.location.reload();
  });
}

// Khởi chạy tất cả
document.addEventListener("DOMContentLoaded", async () => {
  await loadLayout(); // Gọi xong layout thì DOM mới có auth-popup
  initApp();
  initCartSlide();
  initAuthPopup(); // Xử lý hiển thị login/signup
  initRegisterForm(); // Xử lý submit form đăng ký
  initLoginForm();
  renderUserHeader();
});
