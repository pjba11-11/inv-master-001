package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.CustomerDTO;
import com.inv.invmaster001.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO toDto(Customer entity);
    Customer toEntity(CustomerDTO dto);
    void updateEntityFromDto(CustomerDTO dto, @MappingTarget Customer entity);
}