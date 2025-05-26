
export function renderProductsToSlider(products, containerSelector = ".main-slider") {
  const container = document.querySelector(containerSelector);
  if (!container) return;


  let html = `
    <button class="prev slick-arrow">
				<i class="icon icon-arrow-left"></i>
		</button>

    <div class="slider-wrapper">
  `;


  products.forEach((product) => {
    html += `
      <div class="slider-item">
        <div class="banner-content">
          <h2 class="banner-title">${product.name}</h2>
          <p>${product.description || ""}</p>
          <div class="btn-wrap">
            <a href="single-product.html?id=${product.id}" class="btn btn-outline-accent btn-accent-arrow">
              Read More <i class="icon icon-ns-arrow-right"></i>
            </a>
          </div>
        </div>
        <img src="${product.image}" alt="${product.name}" class="banner-image">
      </div>
    `;
  });

  html += `
    </div> 
    <button class="next slick-arrow">
				<i class="icon icon-arrow-right"></i>
		</button>
  `;

  container.innerHTML = html;

  const sliderWrapper = container.querySelector(".slider-wrapper");

  $(sliderWrapper).slick({
    arrows: true,
    autoplay: true,
    autoplaySpeed: 4000,
    dots: true,
    prevArrow: container.querySelector(".prev"),
    nextArrow: container.querySelector(".next"),
  });
}
