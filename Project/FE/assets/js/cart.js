export function initCartSlide() {
  const cartButton = document.querySelector('.cart-button-toggle');
  const cartWrapper = document.getElementById('cart-wrapper');
  const overlay = document.getElementById('overlay');
  const closeCart = document.getElementById('close-cart');
  const cartBody = document.getElementById('cart-body');
  const cartCount = document.getElementById('cart-count');
  const cartTotal = document.getElementById('cart-total');
  const emptyCart = document.getElementById('empty-cart');

  let cart = JSON.parse(localStorage.getItem('cart')) || [];

  function renderCart() {
    if (!cartBody || !cartCount || !cartTotal || !emptyCart) return;

    cartBody.innerHTML = '';
    if (cart.length === 0) {
      emptyCart.style.display = 'block';
      cartCount.textContent = '0';
      cartTotal.textContent = '0.00';
      return;
    }

    emptyCart.style.display = 'none';
    let total = 0;
    cart.forEach((item, index) => {
      total += item.price;
      const div = document.createElement('div');
      div.className = 'cart-item';
      div.innerHTML = `
        <img src="${item.image}" alt="${item.name}" />
        <div class="cart-item-info">
          <div>${item.name}</div>
          <div>$${item.price.toFixed(2)}</div>
        </div>
        <button onclick="removeItem(${index})">×</button>
      `;
      cartBody.appendChild(div);
    });

    cartCount.textContent = cart.length;
    cartTotal.textContent = total.toFixed(2);
  }

  window.addToCart = function(product) {
    cart.push(product);
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCart();
  }

  window.removeItem = function(index) {
    cart.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCart();
  }

  if (cartButton && cartWrapper && overlay) {
    cartButton.addEventListener('click', (e) => {
      e.preventDefault();
      cartWrapper.classList.add('active');
      overlay.classList.add('active');
    });

    closeCart?.addEventListener('click', () => {
      cartWrapper.classList.remove('active');
      overlay.classList.remove('active');
    });

    overlay.addEventListener('click', () => {
      cartWrapper.classList.remove('active');
      overlay.classList.remove('active');
    });
  }

  renderCart();
}
