export function renderBestSeliing(product) {
    const container = document.getElementById(`best-selling-content`);
    if(!container || !product) return;

    container.innerHTML = `
        <div class="row">
            <div class="col-md-6">
                <figure class="products-thumb">
                    <img src="${product.image || './assets/images/single-image.jpg'}" alt="${product.name}" class="single-image">
                </figure>
            </div>

            <div class="col-md-6">
                <div class="product-entry">
                    <h2 class="section-title divider">Best Selling Book</h2>
                    <div class="products-content">
                        <div class="author-name">By ${product.authorName || 'Unknown Author'}</div>
                        <h3 class="item-title">${product.name}</h3>
                        <p>${product.description || 'No description available.'}</p>
                        <div class="item-price">${product.price.toLocaleString('vi-VN')}₫</div>
                        <div class="btn-wrap">
                            <a href="#" class="btn-accent-arrow">shop it now <i class="icon icon-ns-arrow-right"></i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `
}