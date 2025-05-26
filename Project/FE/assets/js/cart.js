// cart.js
const API = "http://127.0.0.1:8080";

// Chuyển số thành định dạng VNĐ
function formatCurrency(v) {
  let num = Number(v);
  if (isNaN(num)) num = 0;
  return num.toLocaleString("vi-VN") + " ₫";
}

export function initCartSlide() {
  // Các phần tử trong DOM
  const cartButton = document.querySelector(".cart-button-toggle");
  const cartBadge = document.getElementById("cart-badge");
  const overlay = document.getElementById("overlay");
  const cartWrapper = document.getElementById("cart-wrapper");
  const closeCart = document.getElementById("close-cart");
  const cartBody = document.getElementById("cart-body");
  const emptyCart = document.getElementById("empty-cart");
  const cartFooterTotal = document.querySelector(
    "#cart-wrapper .cart-footer span"
  );

  // Lấy count + total từ backend
  async function fetchCartInfo() {
    try {
      const [cRes, tRes] = await Promise.all([
        fetch(`${API}/cart/count`, { credentials: "include" }),
        fetch(`${API}/cart/total`, { credentials: "include" }),
      ]);
      if (!cRes.ok || !tRes.ok) throw new Error("API error");
      const count = await cRes.json();
      const total = await tRes.json();
      return { count, total };
    } catch (e) {
      console.warn("Lỗi fetchCartInfo:", e);
      return { count: 0, total: 0 };
    }
  }

  // Lấy chi tiết các item trong cart
  async function fetchCartDetails() {
    try {
      const res = await fetch(`${API}/cart`, { credentials: "include" });
      console.log("[fetchCartDetails] status =", res.status);

      if (res.status === 401) {
        console.warn("Chưa đăng nhập hoặc session hết hạn");
        return [];
      }
      if (!res.ok) {
        throw new Error(`API error: ${res.status}`);
      }

      const cart = await res.json();
      return cart.orderDetails || [];
    } catch (e) {
      console.warn("Lỗi fetchCartDetails:", e);
      return [];
    }
  }

  // Render badge + popup
  async function renderCart() {
    const { count, total } = await fetchCartInfo();

    // badge
    if (cartBadge) {
      cartBadge.textContent = `Giỏ hàng:(${count} | ${formatCurrency(total)})`;
    }

    if (!cartBody || !emptyCart || !cartFooterTotal) return;

    if (count === 0) {
      emptyCart.style.display = "block";
      cartBody.innerHTML = "";
      document.getElementById("cart-subtotal").textContent = formatCurrency(0);
      return;
    }
    emptyCart.style.display = "none";

    const details = await fetchCartDetails();
    cartBody.innerHTML = ""; // Xóa cũ

    details.forEach((item) => {
      const div = document.createElement("div");
      div.className = "cart-item d-flex align-items-center mb-3";

      div.innerHTML = `
      <img src="${item.productImage || "images/default.jpg"}"
           alt="${item.productName}"
           style="width:60px;height:60px;object-fit:cover;border-radius:6px;" />
      <div class="cart-item-info flex-grow-1 ms-3">
        <h6 class="mb-1">${item.productName}</h6>
        <div class="d-flex align-items-center gap-2">
          <span>${formatCurrency(item.price)}</span>
          <input type="number" min="1" value="${item.quantity}"
                 class="cart-qty-input form-control form-control-sm"
                 style="width:60px;"
                 data-item-id="${item.id}" />
        </div>
        <a href="#" class="cart-remove text-danger small" data-item-id="${
          item.id
        }">Remove</a>
      </div>
      <div class="cart-item-total ms-auto fw-semibold">${formatCurrency(
        item.total
      )}</div>
    `;

      // Xử lý change quantity
      div
        .querySelector(".cart-qty-input")
        .addEventListener("change", async (e) => {
          const newQty = parseInt(e.target.value);
          if (newQty < 1) return;
          await fetch(
            `${API}/cart/items/${item.id}/quantity?quantity=${newQty}`,
            {
              method: "PATCH",
              credentials: "include",
            }
          );
          renderCart();
        });

      div
        .querySelector(".cart-qty-input")
        .addEventListener("change", async (e) => {
          const newQty = parseInt(e.target.value, 10);
          if (newQty < 1) return;

          // (1) Gửi yêu cầu lên server để lưu quantity
          const res = await fetch(
            `${API}/cart/items/${item.id}/quantity?quantity=${newQty}`,
            {
              method: "PATCH",
              credentials: "include",
            }
          );
          if (!res.ok) {
            alert("Cập nhật thất bại!");
            return;
          }

          // (2) Tính lại tổng cho dòng này và cập nhật DOM
          const lineTotalEl = e.target
            .closest(".cart-item")
            .querySelector(".cart-item-total");
          const newLineTotal = item.price * newQty;
          lineTotalEl.textContent = formatCurrency(newLineTotal);

          // (3) Lấy lại tổng tiền toàn giỏ hàng và cập nhật badge + footer
          const { count, total: newSubtotal } = await fetchCartInfo();
          cartBadge.textContent = `Giỏ hàng:(${count} ${formatCurrency(
            newSubtotal
          )})`;
          document.getElementById("cart-subtotal").textContent =
            formatCurrency(newSubtotal);
        });

      // Xử lý remove
      div.querySelector(".cart-remove").addEventListener("click", async (e) => {
        e.preventDefault();
        await fetch(`${API}/cart/items/${item.id}`, {
          method: "DELETE",
          credentials: "include",
        });
        renderCart();
      });

      cartBody.appendChild(div);
    });

    // Cập nhật subtotal
    document.getElementById("cart-subtotal").textContent =
      formatCurrency(total);
  }

  // Mở / đóng popup
  if (cartButton && cartWrapper && overlay) {
    cartButton.addEventListener("click", (e) => {
      e.preventDefault();
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

  // Lắng nghe sự kiện từ nút Add-to-Cart
  window.addEventListener("cartUpdated", renderCart);

  // Lần đầu khởi tạo
  renderCart();
}
