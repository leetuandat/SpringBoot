import { fetchBooks } from "./api/booksApi.js";
import { initApp } from "./fetchComponent/initApp.js";
import { loadComponent } from "./fetchComponent/loadComponents.js";
import { renderBooksToSlider } from "./ui/renderBooks.js";


document.addEventListener("DOMContentLoaded", async () => {
    await loadComponent("header-placeholder", "/assets/components/header.html")
    const books = await fetchBooks();
    renderBooksToSlider(books);
    initApp();
});

