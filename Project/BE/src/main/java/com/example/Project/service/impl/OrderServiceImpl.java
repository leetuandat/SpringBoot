package com.example.Project.service.impl;

import com.example.Project.dto.OrderDTO;
import com.example.Project.entity.Order;
import com.example.Project.entity.Customer;
import com.example.Project.entity.PaymentMethod;
import com.example.Project.entity.TransportMethod;
import com.example.Project.mapper.OrderMapper;
import com.example.Project.repository.OrderRepository;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.PaymentMethodRepository;
import com.example.Project.repository.TransportMethodRepository;
import com.example.Project.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransportMethodRepository transportMethodRepository;

    @Override
    public Page<OrderDTO> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDTO> findByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderDTO save(OrderDTO dto, Long userId) {
        // Lấy customer theo userId để gán vào đơn hàng
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Order order = orderMapper.toEntity(dto);
        order.setCustomer(customer);
        order.setOrdersDate(LocalDateTime.now());
        order.setIsActive(true);
        order.setNameReceiver(dto.getNameReceiver());

        // Nếu DTO có các trường liên quan đến PaymentMethod, TransportMethod, bạn cũng có thể set ở đây
        if (dto.getPaymentId() != null) {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.getPaymentId())
                    .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));
            order.setPaymentMethod(paymentMethod);
        }

        if (dto.getTransportId() != null) {
            TransportMethod transportMethod = transportMethodRepository.findById(dto.getTransportId())
                    .orElseThrow(() -> new EntityNotFoundException("Transport method not found"));
            order.setTransportMethod(transportMethod);
        }

        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    public void activateByOrderCode(String orderCode) {
        Order order = orderRepository.findByIdOrders(orderCode)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderCode));
        order.setIsActive(true);
        orderRepository.save(order);
    }

    @Override
    public OrderDTO update(Long id, OrderDTO dto, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        checkUserAccess(order, userId);

        order.setIdOrders(dto.getOrderCode());
        order.setNotes(dto.getNotes());
        order.setNameReceiver(dto.getNameReceiver());
        order.setAddress(dto.getAddress());
        order.setEmail(dto.getEmail());
        order.setPhone(dto.getPhone());
        order.setIsActive(dto.getIsActive());
        order.setTotalMoney(dto.getTotalMoney());


        // Lấy entity PaymentMethod theo id
        PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));
        order.setPaymentMethod(paymentMethod);

        // Lấy entity TransportMethod theo id
        TransportMethod transportMethod = transportMethodRepository.findById(dto.getTransportId())
                .orElseThrow(() -> new EntityNotFoundException("Transport method not found"));
        order.setTransportMethod(transportMethod);


        Order updated = orderRepository.save(order);
        return orderMapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        checkUserAccess(order, userId);

        // Soft delete: đặt isActive = false hoặc trường tương tự
        order.setIsActive(false);
        orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> findByOrderId(String orderId) {
        List<Order> orders = orderRepository.findByIdOrdersContaining(orderId);
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateStatus(Long id, Boolean isActive, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        checkUserAccess(order, userId);

        order.setIsActive(isActive);
        Order updated = orderRepository.save(order);
        return orderMapper.toDto(updated);
    }

    @Override
    public OrderDTO confirmOrder(Long id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        checkUserAccess(order, userId);

        // Logic confirm order: chuyển trạng thái, ghi log, etc.
        order.setIsActive(true);
        // Ví dụ cập nhật trạng thái đơn hàng confirm

        Order confirmed = orderRepository.save(order);
        return orderMapper.toDto(confirmed);
    }

    @Override
    public OrderDTO cancelOrder(Long id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        checkUserAccess(order, userId);

        // Logic hủy đơn hàng
        order.setIsActive(false);
        Order cancelled = orderRepository.save(order);
        return orderMapper.toDto(cancelled);
    }

    @Override
    public Object getOrderStatistics(Long userId) {
        // Ví dụ: trả về tổng số đơn hàng, đơn hàng đã giao, đơn hàng đang xử lý, ...
        long totalOrders = orderRepository.countByCustomerId(userId);
        long activeOrders = orderRepository.countByCustomerIdAndIsActiveTrue(userId);
        long cancelledOrders = orderRepository.countByCustomerIdAndIsActiveFalse(userId);

        return Map.of(
                "totalOrders", totalOrders,
                "activeOrders", activeOrders,
                "cancelledOrders", cancelledOrders
        );
    }

    private void checkUserAccess(Order order, Long userId) {
        if (!order.getCustomer().getId().equals(userId)) {
            throw new SecurityException("Access denied for user");
        }
    }
}
