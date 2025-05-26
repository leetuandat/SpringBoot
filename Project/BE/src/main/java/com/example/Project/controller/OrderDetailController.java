/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/16/2025
 * @time: 06:15 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.OrderDetailDTO;
import com.example.Project.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("order-details")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    /**
     * Lấy tất cả chi tiết đơn hàng với phân trang (Admin only)
     */
    @GetMapping
    public ResponseEntity<Page<OrderDetailDTO>> getAllOrderDetails(Pageable pageable) {
        Page<OrderDetailDTO> orderDetails = orderDetailService.findAll(pageable);
        return ResponseEntity.ok(orderDetails);
    }

    /**
     * Lấy chi tiết đơn hàng theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> getOrderDetailById(@PathVariable Long id) {
        try {
            OrderDetailDTO orderDetail = orderDetailService.findById(id);
            return ResponseEntity.ok(orderDetail);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Thêm sản phẩm vào đơn hàng
     */
    @PostMapping("/order/{orderId}")
    public ResponseEntity<OrderDetailDTO> addProductToOrder(@PathVariable Long orderId,
                                                            @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetailDTO createdOrderDetail = orderDetailService.save(orderId, orderDetailDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDetail);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật chi tiết đơn hàng (số lượng, giá, tổng tiền)
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> updateOrderDetail(@PathVariable Long id,
                                                            @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetailDTO updatedOrderDetail = orderDetailService.update(id, orderDetailDTO);
            return ResponseEntity.ok(updatedOrderDetail);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa sản phẩm khỏi đơn hàng
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeProductFromOrder(@PathVariable Long id) {
        try {
            orderDetailService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy tất cả sản phẩm trong một đơn hàng
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        try {
            List<OrderDetailDTO> orderDetails = orderDetailService.findByOrderId(orderId);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ hàng
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<OrderDetailDTO> updateProductQuantity(@PathVariable Long id,
                                                                @RequestParam Integer quantity) {
        try {
            OrderDetailDTO currentOrderDetail = orderDetailService.findById(id);

            // Tính toán lại tổng tiền
            java.math.BigDecimal newTotal = currentOrderDetail.price()
                    .multiply(java.math.BigDecimal.valueOf(quantity));

            OrderDetailDTO updatedOrderDetailData = new OrderDetailDTO(
                    currentOrderDetail.id(),
                    currentOrderDetail.productId(),
                    currentOrderDetail.price(),
                    quantity,
                    newTotal,
                    currentOrderDetail.productName(),
                    currentOrderDetail.productImage()
            );

            OrderDetailDTO updatedOrderDetail = orderDetailService.update(id, updatedOrderDetailData);
            return ResponseEntity.ok(updatedOrderDetail);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật giá sản phẩm trong đơn hàng
     */
    @PatchMapping("/{id}/price")
    public ResponseEntity<OrderDetailDTO> updateProductPrice(@PathVariable Long id,
                                                             @RequestParam java.math.BigDecimal price) {
        try {
            OrderDetailDTO currentOrderDetail = orderDetailService.findById(id);

            // Tính toán lại tổng tiền
            java.math.BigDecimal newTotal = price
                    .multiply(java.math.BigDecimal.valueOf(currentOrderDetail.quantity()));

            OrderDetailDTO updatedOrderDetailData = new OrderDetailDTO(
                    currentOrderDetail.id(),
                    currentOrderDetail.productId(),
                    price,
                    currentOrderDetail.quantity(),
                    newTotal,
                    currentOrderDetail.productImage(),
                    currentOrderDetail.productName()
            );

            OrderDetailDTO updatedOrderDetail = orderDetailService.update(id, updatedOrderDetailData);
            return ResponseEntity.ok(updatedOrderDetail);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}