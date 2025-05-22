import { renderProductItem } from "./renderProductItem.js";

/**
 * Render danh sách sản phẩm ra HTML container có id #featured-products-list
 * @param {Array} products - Danh sách sản phẩm từ API
 */
export function renderFeaturedProducts(products = []) {
  const container = document.getElementById("featured-products-list");
  if (!container || !Array.isArray(products)) return;

  container.innerHTML = "";

  products.forEach(product => {
    container.insertAdjacentHTML("beforeend", renderProductItem(product));
  });
}
