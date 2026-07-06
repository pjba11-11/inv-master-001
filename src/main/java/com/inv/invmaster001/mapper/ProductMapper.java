package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.ProductDTO;
import com.inv.invmaster001.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO toDto(Product entity);
    Product   toEntity(ProductDTO dto);
    void      updateEntityFromDto(ProductDTO dto, @MappingTarget Product entity);
}