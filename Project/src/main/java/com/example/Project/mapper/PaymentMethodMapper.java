/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/9/2025
 * @time: 12:17 AM
 * @package: com.example.Project.mapper
 */

package com.example.Project.mapper;


import com.example.Project.dto.PaymentMethodDTO;
import com.example.Project.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {
    PaymentMethodDTO toDto(PaymentMethod entity);
    PaymentMethod toEntity(PaymentMethodDTO dto);
    List<PaymentMethod> toEntityList(List<PaymentMethodDTO> dtos);
    List<PaymentMethodDTO> toDtoList(List<PaymentMethod> entities);
}
