export function renderSpecialOffer(products = []) {
  const container = document.getElementById("special-offer-list");
  if (!container || !Array.isArray(products)) return;

  // Nếu đã được slick init trước đó thì phải destroy
  if ($(container).hasClass("slick-initialized")) {
    $(container).slick("unslick");
  }

  container.innerHTML = "";

  products.forEach(product => {
    const slide = document.createElement("div");
    slide.className = "product-item";
    slide.innerHTML = `
      <figure class="product-style">
        <img src="${product.image || './assets/images/default.jpg'}" alt="${product.name}" class="product-item">
        <button type="button" class="add-to-cart" data-product-tile="add-to-cart">Add to Cart</button>
      </figure>
      <figcaption>
        <h3>${product.name}</h3>
        <span>${product.authorName || 'Unknown Author'}</span>
        <div class="item-price">
          <span class="prev-price">₫ 200000</span>
          ${product.price.toLocaleString('vi-VN')}₫
        </div>
      </figcaption>
    `;
    container.appendChild(slide);
  });

  $(container).slick({
    slidesToShow: 4,
    slidesToScroll: 1,
    arrows: true,
    dots: false,
    infinite: false,
    responsive: [
      {
        breakpoint: 992,
        settings: { slidesToShow: 2 }
      },
      {
        breakpoint: 576,
        settings: { slidesToShow: 1 }
      }
    ]
  });
}
