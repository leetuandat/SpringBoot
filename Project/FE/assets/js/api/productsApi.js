import { API_PRODUCT } from "../constant.js";
import { fetchApi } from "./fetchApi.js";

export async function fetchProducts() {
    try {
        const res = await fetchApi(`${API_PRODUCT}?page=0&size=1000`);
        return res?.content || [];
    } catch (error) {
        console.error("Lỗi khi gọi API books: ", error);
        return [];
    }
}

export async function fetchProductsByIds(ids = []) {
  if (!Array.isArray(ids) || ids.length === 0) return [];
  const query = ids.map(id => `ids=${id}`).join("&");
  const url = `${API_PRODUCT}/multi?${query}`;
  return await fetchApi(url);
}

export async function fetchProductsByGenre(categoryName) {
    const url = categoryName
        ? `${API_PRODUCT}/category?category=${encodeURIComponent(categoryName)}`
        : `${API_PRODUCT}`;
    return (await fetchApi(url))?.content || [];
}

