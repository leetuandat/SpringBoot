<script>
  // dashboard.js

const API = "http://127.0.0.1:8080"; // hoặc URL backend của bạn

// Utility
const q  = sel => document.querySelector(sel);
const qa = sel => Array.from(document.querySelectorAll(sel));

// Hiển thị tab
function showSection(name) {
  qa('.content-section').forEach(s => s.classList.remove('active'));
  q(`#${name}-section`).classList.add('active');
}

// Gán active cho tab
function activateTab(el) {
  qa('.nav-link').forEach(l => l.classList.remove('active'));
  el.classList.add('active');
}

// Khởi tạo navigation
function initTabs() {
  qa('.nav-link').forEach(link => {
    link.addEventListener('click', () => {
      activateTab(link);
      showSection(link.dataset.section);
    });
  });
  // show tab đầu tiên (Dashboard)
  const init = q('.nav-link.active');
  if (init) showSection(init.dataset.section);
}

// Định dạng ngày
function fmtDate(dateStr) {
  return new Date(dateStr).toLocaleDateString('vi-VN');
}

// ————————————————————————————
// 1. FETCH VÀ RENDER PRODUCTS
// ————————————————————————————
async function fetchProducts() {
  const res = await fetch(`${API}/products`);
  return res.ok ? res.json() : [];
}

async function loadProducts() {
  const products = await fetchProducts();
  const res = await fetch(`${API}/products`);
  const json = await res.json();
  console.log('PRODUCTS RESPONSE:', json);
  q('#productsTableBody').innerHTML = products.map(p=>`
    <tr>
      <td>${p.id}</td>
      <td><img src="${p.image}" width="50" height="75" class="rounded"></td>
      <td>${p.title}</td>
      <td>${p.author}</td>
      <td><span class="badge bg-primary">${p.categoryName}</span></td>
      <td>${p.price.toLocaleString('vi-VN')} ₫</td>
      <td>${p.stock}</td>
      <td>
        <button class="btn btn-sm btn-warning-custom" onclick="openProductModal(${p.id})">
          <i class="fas fa-edit"></i>
        </button>
        <button class="btn btn-sm btn-danger-custom" onclick="deleteProduct(${p.id})">
          <i class="fas fa-trash"></i>
        </button>
      </td>
    </tr>
  `).join('');
}

// ————————————————————————————
// 2. CRUD CHO PRODUCTS (ADD/EDIT/DELETE)
// ————————————————————————————
function openProductModal(id) {
  const modal = new bootstrap.Modal(q('#productModal'));
  const form  = q('#productForm');
  form.reset();
  q('#productId').value = id||'';
  q('#productModalTitle').textContent = id ? 'Edit Product' : 'Add Product';

  if (id) {
    fetch(`${API}/products/${id}`)
      .then(r=>r.json())
      .then(p=>{
        q('#productTitle').value       = p.title;
        q('#productAuthor').value      = p.author;
        q('#productCategory').value    = p.categoryId;
        q('#productPrice').value       = p.price;
        q('#productStock').value       = p.stock;
        q('#productImage').value       = p.image;
        q('#productDescription').value = p.description;
      });
  }
  modal.show();
}

async function saveProduct() {
  const id = q('#productId').value;
  const body = {
    title:       q('#productTitle').value,
    author:      q('#productAuthor').value,
    categoryId:  +q('#productCategory').value,
    price:       +q('#productPrice').value,
    stock:       +q('#productStock').value,
    image:       q('#productImage').value,
    description: q('#productDescription').value
  };
  const url    = id ? `${API}/products/${id}` : `${API}/products`;
  const method = id ? 'PUT' : 'POST';

  await fetch(url, {
    method,
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(body)
  });
  bootstrap.Modal.getInstance(q('#productModal')).hide();
  loadProducts();
}

async function deleteProduct(id) {
  if (!confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) return;
  await fetch(`${API}/products/${id}`, { method:'DELETE' });
  loadProducts();
}

// ————————————————————————————
// 3. FETCH VÀ RENDER CATEGORIES
// ————————————————————————————
async function fetchCategories() {
  const res = await fetch(`${API}/categories`);
  return res.ok ? res.json() : [];
}

async function loadCategories() {
  const cats = await fetchCategories();
  const res  = await fetch(`${API}/categories`);
  const json = await res.json();
  const list = Array.isArray(json) ? json : (Array.isArray(json.content) ? json.content : []);
  q('#categoriesTableBody').innerHTML = cats.map(c=>`
    <tr>
      <td>${c.id}</td>
      <td>${c.name}</td>
      <td>${c.description}</td>
      <td>${c.productCount}</td>
      <td>
        <button class="btn btn-sm btn-warning-custom" onclick="openCategoryModal(${c.id})">
          <i class="fas fa-edit"></i>
        </button>
        <button class="btn btn-sm btn-danger-custom" onclick="deleteCategory(${c.id})">
          <i class="fas fa-trash"></i>
        </button>
      </td>
    </tr>
  `).join('');
}

// ————————————————————————————
// 4. CRUD CHO CATEGORIES
// ————————————————————————————
function openCategoryModal(id) {
  const modal = new bootstrap.Modal(q('#categoryModal'));
  const form  = q('#categoryForm');
  form.reset();
  q('#categoryId').value = id||'';
  q('#categoryModalTitle').textContent = id ? 'Edit Category' : 'Add Category';

  if (id) {
    fetch(`${API}/categories/${id}`)
      .then(r=>r.json())
      .then(c=>{
        q('#categoryName').value        = c.name;
        q('#categoryDescription').value = c.description;
      });
  }
  modal.show();
}

async function saveCategory(){
  const id = q('#categoryId').value;
  const body = {
    name:        q('#categoryName').value,
    description: q('#categoryDescription').value
  };
  const url    = id ? `${API}/categories/${id}` : `${API}/categories`;
  const method = id ? 'PUT' : 'POST';
  await fetch(url, {
    method, headers:{'Content-Type':'application/json'}, body:JSON.stringify(body)
  });
  bootstrap.Modal.getInstance(q('#categoryModal')).hide();
  loadCategories();
}

async function deleteCategory(id){
  if (!confirm('Xóa danh mục?')) return;
  await fetch(`${API}/categories/${id}`, { method:'DELETE' });
  loadCategories();
}

// ————————————————————————————
// 5. FETCH VÀ RENDER ORDERS
// ————————————————————————————
async function fetchOrders() {
  const res = await fetch(`${API}/orders`);
  return res.ok ? res.json() : [];
}

async function loadOrders() {
  const ords = await fetchOrders();
  q('#ordersTableBody').innerHTML = ords.map(o=>`
    <tr>
      <td>${o.orderCode}</td>
      <td>${o.userName}</td>
      <td>${fmtDate(o.createdAt)}</td>
      <td>${o.totalItems}</td>
      <td>${o.totalAmount.toLocaleString('vi-VN')} ₫</td>
      <td><span class="badge-status badge-${o.status}">${o.status}</span></td>
      <td>
        <button class="btn btn-sm btn-primary-custom" onclick="viewOrder('${o.orderCode}')">
          <i class="fas fa-eye"></i>
        </button>
      </td>
    </tr>
  `).join('');
}

// ————————————————————————————
// 6. FETCH VÀ RENDER USERS (ROLE_USER)
// ————————————————————————————
async function fetchCustomers() {
  const res = await fetch(`${API}/users?role=ROLE_USER`);
  return res.ok ? res.json() : [];
}

async function loadCustomers() {
  const users = await fetchCustomers();
  q('#customersTableBody').innerHTML = users.map(u=>`
    <tr>
      <td>${u.id}</td>
      <td>${u.name}</td>
      <td>${u.email}</td>
      <td>${u.phone}</td>
      <td>${u.orderCount}</td>
      <td>${u.totalSpent.toLocaleString('vi-VN')} ₫</td>
      <td>
        <button class="btn btn-sm btn-info" onclick="viewCustomerOrders(${u.id})">
          <i class="fas fa-eye"></i>
        </button>
      </td>
    </tr>
  `).join('');
}

// ————————————————————————————
// 7. XEM CHI TIẾT ĐƠN & ĐƠN CỦA KHÁCH
// ————————————————————————————
function viewOrder(code) {
  fetch(`${API}/orders/${code}`)
    .then(r=>r.json())
    .then(o=>{
      q('#orderModalBody').innerHTML = `
        <h5>Order ${o.orderCode}</h5>
        <p><strong>User:</strong> ${o.userName}</p>
        <p><strong>Date:</strong> ${fmtDate(o.createdAt)}</p>
        <p><strong>Items:</strong> ${o.orderDetails.length}</p>
        <p><strong>Total:</strong> ${o.totalAmount.toLocaleString('vi-VN')} ₫</p>
        <p><strong>Status:</strong> ${o.status}</p>
      `;
      new bootstrap.Modal(q('#orderModal')).show();
    });
}

function viewCustomerOrders(userId) {
  fetch(`${API}/users/${userId}/orders`)
    .then(r=>r.json())
    .then(list=>{
      q('#customerOrdersTitle').textContent = `Orders of User ${userId}`;
      q('#customerOrdersBody').innerHTML = list.map(o=>`
        <tr>
          <td>${o.orderCode}</td>
          <td>${fmtDate(o.createdAt)}</td>
          <td>${o.orderDetails.length}</td>
          <td>${o.totalAmount.toLocaleString('vi-VN')} ₫</td>
          <td>${o.status}</td>
          <td>
            <button class="btn btn-sm btn-primary-custom" onclick="viewOrder('${o.orderCode}')">
              <i class="fas fa-eye"></i>
            </button>
          </td>
        </tr>
      `).join('');
      new bootstrap.Modal(q('#customerOrdersModal')).show();
    });
}

// ————————————————————————————
// 8. KHỞI TẠO KHI DOM CONTENT LOADED
// ————————————————————————————
document.addEventListener('DOMContentLoaded', () => {
  initTabs();
  loadProducts();
  loadCategories();
  loadOrders();
  loadCustomers();
  q('#lastUpdate').textContent = new Date().toLocaleString('vi-VN');
});

</script>