package com.example.Project.mapper;

import com.example.Project.dto.CustomerDTO;
import com.example.Project.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO toDto(Customer entity);
    Customer toEntity(CustomerDTO dto);

    List<CustomerDTO> toDtoList(List<Customer> entities);
    List<Customer> toEntityList(List<CustomerDTO> dtos);
}
