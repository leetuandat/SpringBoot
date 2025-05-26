/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/16/2025
 * @time: 07:00 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.OrderDTO;
import com.example.Project.dto.OrderDetailDTO;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
//import java.nio.file.attribute.UserPrincipal;
import com.example.Project.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CustomerRepository customerRepository;

    /**
     * Lấy giỏ hàng hiện tại của user
     */
    @GetMapping
    public ResponseEntity<OrderDTO> getCurrentCart(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO cart = cartService.getCurrentCart(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @PostMapping("/items")
    public ResponseEntity<OrderDetailDTO> addToCart(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                                    Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDetailDTO addedItem = cartService.addToCart(userId, orderDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ hàng
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<OrderDetailDTO> updateCartItem(@PathVariable Long itemId,
                                                         @Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                                         Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDetailDTO updatedItem = cartService.updateCartItem(userId, itemId, orderDetailDTO);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Cập nhật nhanh số lượng sản phẩm
     */
    @PatchMapping("/items/{itemId}/quantity")
    public ResponseEntity<OrderDetailDTO> updateItemQuantity(@PathVariable Long itemId,
                                                             @RequestParam Integer quantity,
                                                             Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDetailDTO updatedItem = cartService.updateItemQuantity(userId, itemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long itemId,
                                               Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lấy tổng số sản phẩm trong giỏ hàng
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(Authentication authentication) {
        System.out.println("Auth principal: " + authentication.getPrincipal());
        Long userId = getUserIdFromAuth(authentication);
        System.out.println("UserId: " + userId);
        Integer count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Lấy tổng tiền giỏ hàng
     */
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getCartTotal(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        BigDecimal total = cartService.getCartTotal(userId);
        return ResponseEntity.ok(total);
    }

    /**
     * Áp dụng mã giảm giá (nếu có)
     */
    @PostMapping("/apply-coupon")
    public ResponseEntity<OrderDTO> applyCoupon(@RequestParam String couponCode,
                                                Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO updatedCart = cartService.applyCoupon(userId, couponCode);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Xóa mã giảm giá
     */
    @DeleteMapping("/remove-coupon")
    public ResponseEntity<OrderDTO> removeCoupon(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO updatedCart = cartService.removeCoupon(userId);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Kiểm tra tính khả dụng của sản phẩm trong giỏ hàng
     */
    @GetMapping("/validate")
    public ResponseEntity<List<String>> validateCart(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<String> validationErrors = cartService.validateCart(userId);
        return ResponseEntity.ok(validationErrors);
    }

    /**
     * Chuyển giỏ hàng thành đơn hàng chính thức
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestBody CheckoutRequest checkoutRequest,
                                             Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO order = cartService.checkout(userId, checkoutRequest);
        return ResponseEntity.ok(order);
    }

    /**
     * Lưu giỏ hàng để mua sau (wishlist)
     */
    @PostMapping("/save-for-later/{itemId}")
    public ResponseEntity<Void> saveForLater(@PathVariable Long itemId,
                                             Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        cartService.saveForLater(userId, itemId);
        return ResponseEntity.ok().build();
    }

    /**
     * Chuyển sản phẩm từ "mua sau" về giỏ hàng
     */
    @PostMapping("/move-to-cart/{itemId}")
    public ResponseEntity<OrderDetailDTO> moveToCart(@PathVariable Long itemId,
                                                     Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDetailDTO movedItem = cartService.moveToCart(userId, itemId);
        return ResponseEntity.ok(movedItem);
    }

    /**
     * Helper method để lấy userId từ Authentication
     */
    private Long getUserIdFromAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserPrincipal) {
            // Nếu bạn đã customize UserDetails để bao gồm id
            return ((UserPrincipal) principal).getId();
        }
        else if (principal instanceof UserDetails) {
            // Lấy username từ Spring Security UserDetails
            username = ((UserDetails) principal).getUsername();
        }
        else {
            throw new RuntimeException("Invalid user principal type: " + principal.getClass());
        }

        // Lookup Customer để lấy id
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username))
                .getId();
    }

    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuth(Authentication authentication) {
        if(authentication == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        return ResponseEntity.ok("UserId: " + getUserIdFromAuth(authentication));
    }



    /**
     * Inner class cho checkout request
     */
    public static class CheckoutRequest {
        private String nameReceiver;
        private String address;
        private String email;
        private String phone;
        private Long paymentMethodId;
        private Long transportMethodId;
        private String notes;

        // Getters và setters
        public String getNameReceiver() {
            return nameReceiver;
        }

        public void setNameReceiver(String nameReceiver) {
            this.nameReceiver = nameReceiver;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Long getPaymentMethodId() {
            return paymentMethodId;
        }

        public void setPaymentMethodId(Long paymentMethodId) {
            this.paymentMethodId = paymentMethodId;
        }

        public Long getTransportMethodId() {
            return transportMethodId;
        }

        public void setTransportMethodId(Long transportMethodId) {
            this.transportMethodId = transportMethodId;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}