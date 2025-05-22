export function renderProductItem(product) {
  return `
    <div class="col-md-3">
      <div class="product-item">
        <figure class="product-style">
          <img src="${product.image || './assets/images/default.jpg'}" alt="${product.name}" class="product-item">
         <button href="single-product.html?id=${product.id}" type="button" data-product-id="${product.id}" class="add-to-cart" data-product-tile="add-to-cart">Add to Cart</button>
        </figure>
        <figcaption>
          <h3>${product.name}</h3>
          <span>${product.authorName || 'Unknown Author'}</span>
          <div class="item-price">${product.price.toLocaleString('vi-VN')}₫</div>
        </figcaption>
      </div>
    </div>
  `;
}