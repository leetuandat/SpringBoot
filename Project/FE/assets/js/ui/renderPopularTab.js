import { renderProductItem } from "./renderProductItem.js";

export function renderPopularTab(products = [], genreKey) {
  const container = document.querySelector(`#${genreKey}`);
  if (!container) return;

  container.innerHTML = "";
  const sliced = products.slice(0, 8);
  const chunkSize = 4;

  for (let i = 0; i < sliced.length; i += chunkSize) {
    const chunk = sliced.slice(i, i + chunkSize);
    const rowDiv = document.createElement("div");
    rowDiv.className = "row";

    chunk.forEach(product => {
      rowDiv.insertAdjacentHTML("beforeend", renderProductItem(product));
    });

    container.appendChild(rowDiv);
  }
  
}

