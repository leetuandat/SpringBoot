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

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailDTO toDto(OrderDetail entity);
    OrderDetail toEntity(OrderDetailDTO dto);
    List<OrderDetailDTO> toDtoList(List<OrderDetail> entities);
    List<OrderDetail> toEntityList(List<OrderDetailDTO> dtos);
}
