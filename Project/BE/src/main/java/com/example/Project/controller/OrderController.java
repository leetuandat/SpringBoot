/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/16/2025
 * @time: 06:00 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.OrderDTO;
import com.example.Project.entity.Order;
import com.example.Project.mapper.OrderMapper;
import com.example.Project.repository.OrderRepository;
import com.example.Project.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @PostMapping("/activate/{orderCode}")
    public ResponseEntity<Void> activateOrder(
            @PathVariable String orderCode,
            Authentication authentication) {
        // (tùy chọn) có thể kiểm tra quyền user ở đây
        orderService.activateByOrderCode(orderCode);
        return ResponseEntity.ok().build();
    }

    /**
     * Lấy tất cả đơn hàng với phân trang
     */


    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAll(Pageable pageable) {
        // Directly retrieve Page<OrderDTO> from service
        Page<OrderDTO> dtoPage = orderService.findAll(pageable);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Lấy đơn hàng theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Lấy đơn hàng của user hiện tại
     */
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDTO>> getMyOrders(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<OrderDTO> orders = orderService.findByCustomerId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Tạo đơn hàng mới
     */
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO,
                                                Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO createdOrder = orderService.save(orderDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * Cập nhật đơn hàng
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id,
                                                @Valid @RequestBody OrderDTO orderDTO,
                                                Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO updatedOrder = orderService.update(id, orderDTO, userId);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Xóa đơn hàng (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id,
                                            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        orderService.deleteById(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Tìm kiếm đơn hàng theo mã đơn hàng
     */
    @GetMapping("/search")
    public ResponseEntity<List<OrderDTO>> searchOrdersByCode(@RequestParam String orderCode) {
        List<OrderDTO> orders = orderService.findByOrderId(orderCode);
        return ResponseEntity.ok(orders);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id,
                                                      @RequestParam Boolean isActive,
                                                      Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO updatedOrder = orderService.updateStatus(id, isActive, userId);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Xác nhận đơn hàng (chuyển từ giỏ hàng thành đơn hàng chính thức)
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderDTO> confirmOrder(@PathVariable Long id,
                                                 Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO confirmedOrder = orderService.confirmOrder(id, userId);
        return ResponseEntity.ok(confirmedOrder);
    }

    /**
     * Hủy đơn hàng
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id,
                                                Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO cancelledOrder = orderService.cancelOrder(id, userId);
        return ResponseEntity.ok(cancelledOrder);
    }

    /**
     * Lấy thống kê đơn hàng của user
     */
    @GetMapping("/statistics")
    public ResponseEntity<Object> getOrderStatistics(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        Object stats = orderService.getOrderStatistics(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Helper method để lấy userId từ Authentication
     */
    private Long getUserIdFromAuth(Authentication authentication) {
        // Implement logic để lấy userId từ JWT token hoặc UserDetails
        // Ví dụ: return ((UserPrincipal) authentication.getPrincipal()).getId();
        return 1L; // Placeholder - thay thế bằng logic thực tế
    }
}