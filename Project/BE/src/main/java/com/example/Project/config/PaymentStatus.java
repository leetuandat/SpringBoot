package com.example.Project.config;


public enum PaymentStatus {
    UNPAID,     // mặc định sau khi tạo đơn
    PENDING,    // đã tạo giao dịch chờ thanh toán
    PAID,       // thanh toán thành công
    FAILED,     // thanh toán thất bại
    CANCELED    // người dùng hủy
}
