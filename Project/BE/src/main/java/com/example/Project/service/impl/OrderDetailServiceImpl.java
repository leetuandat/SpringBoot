/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/16/2025
 * @time: 05:37 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.OrderDetailDTO;
import com.example.Project.entity.Order;
import com.example.Project.entity.OrderDetail;
import com.example.Project.entity.Product;
import com.example.Project.mapper.OrderDetailMapper;
import com.example.Project.repository.OrderDetailRepository;
import com.example.Project.repository.OrderRepository;
import com.example.Project.repository.ProductRepository;
import com.example.Project.service.OrderDetailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public Page<OrderDetailDTO> findAll(Pageable pageable) {
        return (orderDetailRepository.findAll(pageable)).map(orderDetailMapper::toDto);
    }

    @Override
    public OrderDetailDTO findById(Long id) {
        OrderDetail entity = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        return orderDetailMapper.toDto(entity);
    }

    @Override
    public OrderDetailDTO save(Long orderId, OrderDetailDTO dto) {
        OrderDetail entity = orderDetailMapper.toEntity(dto);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        entity.setOrder(order);
        entity.setProduct(product);

        OrderDetail saved = orderDetailRepository.save(entity);
        return orderDetailMapper.toDto(saved);
    }

    @Override
    public OrderDetailDTO update(Long id, OrderDetailDTO dto) {
        OrderDetail entity = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        entity.setProduct(product);
        entity.setPrice(dto.price());
        entity.setQuantity(dto.quantity());
        entity.setTotal(dto.total());

        return orderDetailMapper.toDto(orderDetailRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        OrderDetail entity = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        orderDetailRepository.delete(entity);
    }

    @Override
    public List<OrderDetailDTO> findByOrderId(Long orderId) {
        return orderDetailMapper.toDtoList(orderDetailRepository.findByOrderId(orderId));
    }

    @Override
    public OrderDetailDTO updateQuantity(Long id, Integer quantity, Long userId) {
        OrderDetail existing = orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"));

        checkUserAccess(existing.getOrder(), userId);

        existing.setQuantity(quantity);
        existing.setTotal(existing.getPrice().multiply(BigDecimal.valueOf(quantity)));

        OrderDetail updated = orderDetailRepository.save(existing);
        return orderDetailMapper.toDto(updated);
    }

    @Override
    public OrderDetailDTO updatePrice(Long id, BigDecimal price, Long userId) {
        OrderDetail existing = orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"));

        // TODO: check if userId has admin rights or similar, only admin allowed
        // checkAdmin(userId);

        existing.setPrice(price);
        existing.setTotal(price.multiply(BigDecimal.valueOf(existing.getQuantity())));

        OrderDetail updated = orderDetailRepository.save(existing);
        return orderDetailMapper.toDto(updated);
    }

    @Override
    public List<OrderDetailDTO> batchUpdate(List<OrderDetailDTO> dtos, Long userId) {
        List<OrderDetailDTO> updatedList = dtos.stream().map(dto -> {
            OrderDetail existing = orderDetailRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found: " + dto.id()));

            checkUserAccess(existing.getOrder(), userId);

            existing.setProductId(dto.productId());
            existing.setPrice(dto.price());
            existing.setQuantity(dto.quantity());
            BigDecimal total = dto.price().multiply(BigDecimal.valueOf(dto.quantity()));
            existing.setTotal(total);

            return orderDetailMapper.toDto(orderDetailRepository.save(existing));
        }).collect(Collectors.toList());

        return updatedList;
    }

    private void checkUserAccess(Order order, Long userId) {
        if (!order.getCustomer().getId().equals(userId)) {
            throw new SecurityException("Access denied for user");
        }
    }
}
