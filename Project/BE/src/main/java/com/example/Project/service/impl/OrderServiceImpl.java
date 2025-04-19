/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/16/2025
 * @time: 05:12 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.OrderDTO;
import com.example.Project.entity.Customer;
import com.example.Project.entity.Order;
import com.example.Project.entity.PaymentMethod;
import com.example.Project.entity.TransportMethod;
import com.example.Project.mapper.OrderMapper;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.OrderRepository;
import com.example.Project.repository.PaymentMethodRepository;
import com.example.Project.repository.TransportMethodRepository;
import com.example.Project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransportMethodRepository transportMethodRepository;

    @Override
    public Page<OrderDTO> findAll(Pageable pageable) {
        return (orderRepository.findAll(pageable)).map(orderMapper::toDto);
    }

    @Override
    public OrderDTO findById(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDto(entity);
    }

    @Override
    public OrderDTO save(OrderDTO dto) {
        Order entity = orderMapper.toEntity(dto);
        entity.setIsDelete(false);
        entity.setIsActive(true);

        // Set foreign keys
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        PaymentMethod payment = paymentMethodRepository.findById(dto.paymentId())
                .orElseThrow(() -> new RuntimeException("Payment Method not found"));
        TransportMethod transport = transportMethodRepository.findById(dto.transportId())
                .orElseThrow(() -> new RuntimeException("Transport Method not found"));

        entity.setCustomer(customer);
        entity.setPaymentMethod(payment);
        entity.setTransportMethod(transport);

        Order saved = orderRepository.save(entity);
        return orderMapper.toDto(saved);
    }

    @Override
    public OrderDTO update(Long id, OrderDTO dto) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        entity.setTotalMoney(dto.totalMoney());
        entity.setNotes(dto.notes());
        entity.setNameReceiver(dto.nameReceiver());
        entity.setAddress(dto.address());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        entity.setIsActive(dto.isActive());

        // Update relationships
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        PaymentMethod payment = paymentMethodRepository.findById(dto.paymentId())
                .orElseThrow(() -> new RuntimeException("Payment Method not found"));
        TransportMethod transport = transportMethodRepository.findById(dto.transportId())
                .orElseThrow(() -> new RuntimeException("Transport Method not found"));

        entity.setCustomer(customer);
        entity.setPaymentMethod(payment);
        entity.setTransportMethod(transport);

        Order updated = orderRepository.save(entity);
        return orderMapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        entity.setIsDelete(true);
        orderRepository.save(entity);
    }

    @Override
    public List<OrderDTO> findByOrderId(String orderId) {
        return orderMapper.toDtoList(orderRepository.findByIdOrdersContainingIgnoreCase(orderId));
    }
}
