package com.example.Project.mapper;


import com.example.Project.dto.OrderDTO;
import com.example.Project.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "idOrders", target = "orderCode")
    @Mapping(source = "ordersDate", target = "orderDate")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "paymentMethod.id", target = "paymentId")
    @Mapping(source = "transportMethod.id", target = "transportId")
    @Mapping(source = "nameReceiver", target = "nameReceiver")
    OrderDTO toDto(Order entity);

    @Mapping(source = "orderCode", target = "idOrders")
    @Mapping(source = "orderDate", target = "ordersDate")
    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "paymentId", target = "paymentMethod.id")
    @Mapping(source = "transportId", target = "transportMethod.id")
    @Mapping(source = "nameReceiver", target = "nameReceiver")
    Order toEntity(OrderDTO dto);

    List<OrderDTO> toDtoList(List<Order> entities);
    List<Order> toEntityList(List<OrderDTO> dtos);
}
