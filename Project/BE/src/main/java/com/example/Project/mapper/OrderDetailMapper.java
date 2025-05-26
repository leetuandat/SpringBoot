/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/9/2025
 * @time: 01:26 AM
 * @package: com.example.Project.mapper
 */

package com.example.Project.mapper;

import com.example.Project.dto.OrderDetailDTO;
import com.example.Project.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(target = "productId", expression = "java(entity.getProduct() != null ? entity.getProduct().getId() : null)"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "total", target = "total"),
            @Mapping(source = "product.name",  target = "productName"),
            @Mapping(source = "product.image", target = "productImage")
    })
    OrderDetailDTO toDto(OrderDetail entity);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(target = "product.id", source = "productId"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "total", target = "total")
    })
    OrderDetail toEntity(OrderDetailDTO dto);

    List<OrderDetailDTO> toDtoList(List<OrderDetail> entities);
    List<OrderDetail> toEntityList(List<OrderDetailDTO> dtos);
}
