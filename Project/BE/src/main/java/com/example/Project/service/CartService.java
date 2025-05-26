package com.example.Project.service;

import com.example.Project.controller.CartController.CheckoutRequest;
import com.example.Project.dto.OrderDTO;
import com.example.Project.dto.OrderDetailDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    OrderDTO getCurrentCart(Long userId);

    OrderDetailDTO addToCart(Long userId, OrderDetailDTO orderDetailDTO);

    OrderDetailDTO updateCartItem(Long userId, Long itemId, OrderDetailDTO orderDetailDTO);

    OrderDetailDTO updateItemQuantity(Long userId, Long itemId, Integer quantity);

    void removeFromCart(Long userId, Long itemId);

    void clearCart(Long userId);

    Integer getCartItemCount(Long userId);

    BigDecimal getCartTotal(Long userId);

    OrderDTO applyCoupon(Long userId, String couponCode);

    OrderDTO removeCoupon(Long userId);

    List<String> validateCart(Long userId);

    OrderDTO checkout(Long userId, CheckoutRequest checkoutRequest);

    void saveForLater(Long userId, Long itemId);

    OrderDetailDTO moveToCart(Long userId, Long itemId);
}
