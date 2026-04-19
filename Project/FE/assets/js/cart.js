// cart.js
// ====== CART SLIDE (tối ưu) ======
const API = "http://127.0.0.1:8080";

// Định dạng VNĐ
function formatCurrency(v) {
  let num = Number(v);
  if (isNaN(num)) num = 0;
  return num.toLocaleString("vi-VN") + " ₫";
}

export function initCartSlide() {
  // ---- Cache element ----
  const cartButton = document.querySelector(".cart-button-toggle");
  const cartBadge = document.getElementById("cart-badge");
  const overlay = document.getElementById("overlay");
  const cartWrapper = document.getElementById("cart-wrapper");
  const closeCart = document.getElementById("close-cart");
  const cartBody = document.getElementById("cart-body");
  const emptyCart = document.getElementById("empty-cart");
  const cartFooterTotal = document.querySelector("#cart-wrapper .cart-footer span");
  const cartSubtotalEl = document.getElementById("cart-subtotal");

  // ---- Local state ----
  const state = {
    items: [],   // {id, productName, productImage, price, quantity, total}
    count: 0,    // tổng số lượng (sum quantity)
    subtotal: 0, // tổng tiền
    loading: false,
  };

  // ---- Helpers ----
  function clearCartUI() {
    if (cartBadge) cartBadge.textContent = `Giỏ hàng:(0 | ${formatCurrency(0)})`;
    if (emptyCart) emptyCart.style.display = "block";
    if (cartBody) cartBody.innerHTML = "";
    if (cartSubtotalEl) cartSubtotalEl.textContent = formatCurrency(0);
  }

  function computeFromItems(items) {
    let count = 0, subtotal = 0;
    for (const it of items) {
      const qty = Number(it.quantity) || 0;
      const price = Number(it.price) || 0;
      const lineTotal = (typeof it.total === "number" ? it.total : price * qty) || 0;
      count += qty;
      subtotal += lineTotal;
    }
    return { count, subtotal };
  }

  function updateBadgeAndFooter() {
    if (cartBadge) {
      cartBadge.textContent = `Giỏ hàng:(${state.count} | ${formatCurrency(state.subtotal)})`;
    }
    if (cartFooterTotal || cartSubtotalEl) {
      // 2 nơi hiển thị subtotal, tuỳ layout bạn dùng cái nào
      cartFooterTotal && (cartFooterTotal.textContent = formatCurrency(state.subtotal));
      cartSubtotalEl && (cartSubtotalEl.textContent = formatCurrency(state.subtotal));
    }
  }

  function renderItems() {
    if (!cartBody || !emptyCart) return;

    // Giỏ trống
    if (!state.items.length) {
      emptyCart.style.display = "block";
      cartBody.innerHTML = "";
      updateBadgeAndFooter();
      return;
    }
    emptyCart.style.display = "none";

    // Render bằng DocumentFragment để mượt
    const frag = document.createDocumentFragment();

    for (const item of state.items) {
      const wrap = document.createElement("div");
      wrap.className = "cart-item d-flex align-items-center mb-3";
      wrap.dataset.itemId = item.id;

      wrap.innerHTML = `
        <img src="${item.productImage || "images/default.jpg"}"
             alt="${item.productName || ""}"
             style="width:60px;height:60px;object-fit:cover;border-radius:6px;" />
        <div class="cart-item-info flex-grow-1 ms-3">
          <h6 class="mb-1">${item.productName || ""}</h6>
          <div class="d-flex align-items-center gap-2">
            <span>${formatCurrency(item.price)}</span>
            <input type="number" min="1" value="${item.quantity}"
                   class="cart-qty-input form-control form-control-sm"
                   style="width:60px;"
                   data-item-id="${item.id}" />
          </div>
          <a href="#" class="cart-remove text-danger small" data-item-id="${item.id}">Remove</a>
        </div>
        <div class="cart-item-total ms-auto fw-semibold">${formatCurrency(item.total ?? (item.price * item.quantity))}</div>
      `;
      frag.appendChild(wrap);
    }

    cartBody.innerHTML = "";
    cartBody.appendChild(frag);
    updateBadgeAndFooter();
  }

  async function loadCart() {
    state.loading = true;
    try {
      const res = await fetch(`${API}/cart`, { credentials: "include", cache: "no-store" });
      if (res.status === 401) {
        // Chưa đăng nhập → clear
        state.items = [];
        const { count, subtotal } = computeFromItems(state.items);
        state.count = count; state.subtotal = subtotal;
        clearCartUI();
        return;
      }
      if (!res.ok) throw new Error(`API /cart error: ${res.status}`);
      const cart = await res.json();
      state.items = Array.isArray(cart?.orderDetails) ? cart.orderDetails : [];
      const { count, subtotal } = computeFromItems(state.items);
      state.count = count; state.subtotal = subtotal;
      renderItems();
    } catch (e) {
      console.warn("loadCart error:", e);
      // giữ trạng thái cũ nhưng cập nhật UI để không treo
      renderItems();
    } finally {
      state.loading = false;
    }
  }

  // ---- Event delegation cho body: change qty & remove ----
  cartBody?.addEventListener("change", async (e) => {
    const input = e.target.closest(".cart-qty-input");
    if (!input) return;

    const id = Number(input.dataset.itemId);
    let newQty = parseInt(input.value, 10);
    if (!Number.isFinite(newQty) || newQty < 1) newQty = 1; // về mốc an toàn
    input.value = String(newQty);

    const idx = state.items.findIndex((x) => Number(x.id) === id);
    if (idx === -1) return;

    // Lưu để rollback nếu fail
    const oldQty = state.items[idx].quantity;
    const price = Number(state.items[idx].price) || 0;

    // --- Cập nhật lạc quan UI ---
    state.items[idx].quantity = newQty;
    state.items[idx].total = price * newQty;
    const { count, subtotal } = computeFromItems(state.items);
    state.count = count; state.subtotal = subtotal;

    // Cập nhật dòng
    const lineEl = input.closest(".cart-item")?.querySelector(".cart-item-total");
    if (lineEl) lineEl.textContent = formatCurrency(price * newQty);
    updateBadgeAndFooter();

    // --- Gọi API PATCH ---
    try {
      const res = await fetch(`${API}/cart/items/${id}/quantity?quantity=${newQty}`, {
        method: "PATCH",
        credentials: "include",
        cache: "no-store",
      });
      if (!res.ok) throw new Error(`PATCH qty failed: ${res.status}`);
    } catch (err) {
      console.warn(err);
      // rollback
      state.items[idx].quantity = oldQty;
      state.items[idx].total = price * oldQty;
      const recover = computeFromItems(state.items);
      state.count = recover.count; state.subtotal = recover.subtotal;

      // UI rollback
      input.value = String(oldQty);
      if (lineEl) lineEl.textContent = formatCurrency(price * oldQty);
      updateBadgeAndFooter();
      alert("Cập nhật số lượng thất bại!");
    }
  });

  cartBody?.addEventListener("click", async (e) => {
    const rm = e.target.closest(".cart-remove");
    if (!rm) return;
    e.preventDefault();

    const id = Number(rm.dataset.itemId);
    const idx = state.items.findIndex((x) => Number(x.id) === id);
    if (idx === -1) return;

    // Lạc quan: xoá khỏi state & UI
    const removed = state.items.splice(idx, 1)[0];
    const after = computeFromItems(state.items);
    state.count = after.count; state.subtotal = after.subtotal;
    renderItems();

    try {
      const res = await fetch(`${API}/cart/items/${id}`, {
        method: "DELETE",
        credentials: "include",
        cache: "no-store",
      });
      if (!res.ok) throw new Error(`DELETE failed: ${res.status}`);
      // (tuỳ chọn) phát event cho nơi khác cập nhật
      window.dispatchEvent(new Event("cartUpdated"));
    } catch (err) {
      console.warn(err);
      // rollback nếu xoá thất bại
      state.items.splice(idx, 0, removed);
      const recover = computeFromItems(state.items);
      state.count = recover.count; state.subtotal = recover.subtotal;
      renderItems();
      alert("Xoá sản phẩm thất bại!");
    }
  });

  // ---- Open/Close slide cart ----
  if (cartButton && cartWrapper && overlay) {
    cartButton.addEventListener("click", async (e) => {
      e.preventDefault();
      await loadCart(); // refresh mỗi lần mở
      cartWrapper.classList.add("active");
      overlay.classList.add("active");
    });
    closeCart?.addEventListener("click", () => {
      cartWrapper.classList.remove("active");
      overlay.classList.remove("active");
    });
    overlay.addEventListener("click", () => {
      cartWrapper.classList.remove("active");
      overlay.classList.remove("active");
    });
  }

  // ---- Lắng nghe sự kiện toàn cục ----
  // Khi nơi khác thêm/sửa giỏ → reload
  window.addEventListener("cartUpdated", loadCart);

  // Khi login/logout → clear & reload
  window.addEventListener("authChanged", () => {
    clearCartUI();
    loadCart();
  });

  // ---- Init lần đầu ----
  loadCart();
}
