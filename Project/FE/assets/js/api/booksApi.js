export async function fetchBooks() {
    try {
        const res = await fetch("http://localhost:8080/books?page=0&size=1000");
        const data = await res.json();
        return data.content;
    } catch (error) {
        console.error("Lỗi khi gọi API books: ", error);
        return [];
    }
}