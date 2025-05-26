import { fetchApi } from "./api/fetchApi.js";
import {
  fetchProducts,
  fetchProductsByGenre,
  fetchProductsByIds,
} from "./api/productsApi.js";
import { initCartSlide } from "./cart.js";
import { API_CATEGORY } from "./constant.js";
import { initApp } from "./fetchComponent/initApp.js";
import { loadLayout } from "./fetchComponent/loadLayout.js";
import { renderBestSeliing } from "./ui/renderBestSelling.js";
import { renderFeaturedProducts } from "./ui/renderFeaturedProduct.js";
import { renderPopularTab } from "./ui/renderPopularTab.js";
import { renderProductsToSlider } from "./ui/renderProducts.js";
import { renderSpecialOffer } from "./ui/renderSpecialOffer.js";
import { setupTabs } from "./ui/setupTab.js";

// ✅ ID của 8 sản phẩm hiển thị ở All Genre (cho phép nhập tay)
const allGenreProductIds = [1, 6, 11, 16, 21, 26, 31, 36];

document.addEventListener("DOMContentLoaded", async () => {
  // await loadLayout();
  // initCartSlide();
  const allProducts = await fetchProducts();

  // Featured section
  const featuredIds = [1, 6, 11, 16];
  const featuredBooks = allProducts.filter((p) => featuredIds.includes(p.id));
  renderFeaturedProducts(featuredBooks);

  // Slick carousel section
  renderProductsToSlider(allProducts);

  // Best selling
  const bestSelling = allProducts.find((p) => p.id === 8);
  if (bestSelling) {
    renderBestSeliing(bestSelling);
  }

  // All genre tab (fixed IDs)
  const allGenreProducts = allProducts.filter((p) =>
    allGenreProductIds.includes(p.id)
  );
  renderPopularTab(allGenreProducts, "all-genre");

  // Category tabs
  const response = await fetchApi(`${API_CATEGORY}`);
  const categories = response?.content || [];
  for (const category of categories) {
    const key = category.name.toLowerCase().replace(/\s+/g, "-");
    const products = await fetchProductsByGenre(category.name);
    renderPopularTab(products, key);
  }

  //Special offer
  const offerIds = [1, 6, 11, 16, 21, 26, 31, 36];
  const offerProducts = allProducts.filter(p => offerIds.includes(p.id));


renderSpecialOffer(offerProducts);

  setupTabs();
  // initApp();
});
