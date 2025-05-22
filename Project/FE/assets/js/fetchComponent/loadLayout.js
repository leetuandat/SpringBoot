// 📁 src/fetchComponent/loadLayout.js

import { loadComponent } from "./loadComponents.js";

export async function loadLayout() {
  await loadComponent("header-placeholder", "/assets/components/header.html");
  await loadComponent("footer-placeholder", "/assets/components/footer.html");
  await loadComponent("footer-bottom-placeholder", "/assets/components/footer-bottom.html");
}
