/**
 * Hàm fetch API dùng chung
 * @param {string} api - Đường dẫn đầy đủ (bao gồm cả query)
 * @returns {Promise<any>} - Dữ liệu trả về (hoặc [] nếu lỗi)
 */
export async function fetchApi(api) {
    try {
        const res = await fetch(api);
        if (!res.ok) {
            throw new Error(`Lỗi HTTP: ${res.status}`);
        }
        const data = await res.json();
        return data;
    } catch (error) {
        console.error(`Lỗi khi gọi API [${api}]:`, error);
        return null; 
    }
}
