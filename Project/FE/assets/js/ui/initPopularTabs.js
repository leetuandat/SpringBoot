import { fetchApi } from "../api/fetchApi.js";
import { fetchProductsByGenre } from "../api/productsApi.js";
import { renderPopularTab } from "./renderPopularTabs.js";
import { API_CATEGORY } from "../constant.js";

/**
 * Lấy danh sách category và gọi dữ liệu từng tab
 */
export async function initPopularTabs() {
  const response = await fetchApi(`${API_CATEGORY}`);
  const categories = response?.content || [];

  renderPopularTab(await fetchProductsByGenre(""), "all-genre");

  for (const category of categories) {
    const key = category.name.toLowerCase().replace(/\s+/g, "-");
    const products = await fetchProductsByGenre(category.name);
    renderPopularTab(products, key);
  }
}
