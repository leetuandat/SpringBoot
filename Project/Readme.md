# BookStore - e-commerce

A comprehensive e-commerce platform featuring a robust RESTful API and a responsive user interface, supporting essential operations such as user authentication, product catalog management, order processing, and secure shopping cart functionality.

## Technology Stack

## Frontend: 
- **ReactJS**: The core JavaScript library for building the user interface.
- **Bootstrap**: CSS framework for responsive and mobile-first design.
- **Font Awesome**: Icon toolkit for scalable vector icons.
- **Axios**: Promise-based HTTP client for handling API requests and data fetching.
- **React Router DOM**: The standard routing library for managing navigation in React applications.

## Backend:
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Security** with JWT Authentication
- **Spring Data JPA** with Hibernate
- **MySQL 8.0+** Database
- **Maven** Build Tool
- **Lombok** for reducing boilerplate code
- **JWT (JSON Web Tokens)** for authentication
- **Bean Validation** for request validation

## Features

### 1. Admin User Management
- User registration and authentication
- JWT-based authentication with refresh tokens
- Role-based access control (RBAC)
  - ADMIN: Full system access
  - CUSTOMER: Read-only access, buy and review product
- User profile management (CRUD)
- Password management (change, reset)
- User search and filtering with pagination
- Payment via VNPAY

### 2. Book Category Management
- CRUD Operations: Create, read, update, and delete book categories (e.g., Fiction, Economics, Science, Children's books).
- Category Hierarchy Support: Support for parent-child category structures.
- Display Order Management: Control the display order of categories on the frontend.
- Category Status Control: Enable/disable categories to control visibility.
- Home Page Display Configuration: Manage featured categories for the homepage.
- Search and Filter: Search and filter books by specific categories.

### 3. Book/Product Management
- CRUD Operations: Create, read, update, and delete book information within the system.
- Book Details: Support for comprehensive details: Title, Description, Price, ISBN, Publication Year, and Stock Quantity.
- Inventory/Stock Management: Manage stock status (In-stock, Out-of-stock, Incoming).
- Category Association: Associate books with relevant categories and tags.
- Visual Assets: Support for book covers and thumbnail images.
- Author Attribution: Manage author profiles and assign them to specific books.
- Advanced Filtering: Filter books by author, price range, category, rating, or publication date.
- Pagination and Sorting: Pagination and flexible sorting options (by price, name, latest arrivals).
- Quick Status Updates: Efficiently toggle book status (e.g., On Sale, New Arrival, Bestseller).

### 4. Security Features
- JWT token-based authentication
- Token refresh mechanism
- Role-based authorization
- Password encryption (BCrypt)
- Account lockout after failed attempts
- Secure password reset flow
- CORS configuration
- Global exception handling

## Some UI in my project:
<img width="945" height="449" alt="image" src="https://github.com/user-attachments/assets/3bbd5b24-a8a9-4766-8635-5bd4ec23f8af" />
<img width="945" height="451" alt="image" src="https://github.com/user-attachments/assets/dd5443c8-de38-4407-bce6-cff7f51734d2" />
<img width="945" height="449" alt="image" src="https://github.com/user-attachments/assets/2792e461-b887-4071-81d2-e1d9f2968aa4" />
<img width="945" height="453" alt="image" src="https://github.com/user-attachments/assets/e73979a4-ebe7-4dd3-9879-e21fd88ac7fa" />
<img width="945" height="449" alt="image" src="https://github.com/user-attachments/assets/b392c00c-87e6-4445-9f2b-f4fe402c0eba" />
<img width="975" height="465" alt="image" src="https://github.com/user-attachments/assets/22cd3b10-f502-4ce2-93bd-a183bc55758b" />
<img width="945" height="450" alt="image" src="https://github.com/user-attachments/assets/9f60059c-fb4b-471a-b218-6fd147b2dd64" />
<img width="1903" height="913" alt="image" src="https://github.com/user-attachments/assets/1dcf7232-38f5-4550-b924-e671c9b31b50" />
<img width="945" height="445" alt="image" src="https://github.com/user-attachments/assets/b0e49a03-5784-42f7-9444-8df2c4e5221f" />
<img width="975" height="463" alt="image" src="https://github.com/user-attachments/assets/77bbcf3c-d81c-4f9a-938e-3ba79395fb73" />
<img width="945" height="445" alt="image" src="https://github.com/user-attachments/assets/f818131e-fad4-439c-8c76-2c15266908d9" />
<img width="945" height="442" alt="image" src="https://github.com/user-attachments/assets/47870d27-1caf-4f53-a8d4-bf92d1e0848a" />
<img width="945" height="446" alt="image" src="https://github.com/user-attachments/assets/3df9f726-a833-47c9-83ef-f5200c7af850" />
<img width="945" height="409" alt="image" src="https://github.com/user-attachments/assets/6ce4bf5a-b99c-4f8f-80e1-dfe05973c39b" />
