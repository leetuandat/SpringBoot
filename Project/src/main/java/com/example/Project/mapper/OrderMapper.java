package com.example.Project.mapper;


import com.example.Project.dto.OrderDTO;
import com.example.Project.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDto(Order entity);
    Order toEntity(OrderDTO dto);
    List<OrderDTO> toDtoList(List<Order> entities);
    List<Order> toEntityList(List<OrderDTO> dtos);
}
