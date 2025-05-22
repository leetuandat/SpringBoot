// api.js
const PATH = "http://localhost:8080";
export const API_PRODUCT = `${PATH}/products`;
export const API_CATEGORY = `${PATH}/categories`;

export async function fetchApi(url) {
  const res = await fetch(url);
  return await res.json();
}

export async function fetchProducts() {
  const res = await fetchApi(API_PRODUCT);
  return res?.content || [];
}

export async function fetchProductsByGenre(genre) {
  const url = `${API_PRODUCT}/search?genre=${genre}`;
  const res = await fetchApi(url);
  return res?.content || [];
}

export async function fetchProductsByIds(ids = []) {
  const res = await fetchApi(`${API_PRODUCT}`);
  return (res?.content || []).filter((p) => ids.includes(p.id));
}
