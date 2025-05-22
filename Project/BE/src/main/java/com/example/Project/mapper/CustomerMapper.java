package com.example.Project.mapper;

import com.example.Project.dto.CustomerDTO;
import com.example.Project.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Chuyển từ entity sang DTO
    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.email", target = "email")
    @Mapping(source = "entity.phone", target = "phone")
    @Mapping(source = "entity.address", target = "address")
    @Mapping(source = "entity.avatar", target = "avatar")
    @Mapping(source = "entity.role", target = "role")
    @Mapping(source = "entity.isActive", target = "isActive")
    CustomerDTO toDto(Customer entity);

    // Chuyển từ DTO sang entity
    @Mapping(source = "dto.id", target = "id")
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.email", target = "email")
    @Mapping(source = "dto.phone", target = "phone")
    @Mapping(source = "dto.address", target = "address")
    @Mapping(source = "dto.avatar", target = "avatar")
    @Mapping(source = "dto.role", target = "role")
    @Mapping(source = "dto.isActive", target = "isActive")
    Customer toEntity(CustomerDTO dto);

    // Chuyển danh sách entity sang danh sách DTO
    List<CustomerDTO> toDtoList(List<Customer> entities);

    // Chuyển danh sách DTO sang danh sách entity
    List<Customer> toEntityList(List<CustomerDTO> dtos);
}
