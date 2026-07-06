package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.ProductMaterialDTO;
import com.inv.invmaster001.entity.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMaterialMapper {

    ProductMaterialMapper INSTANCE = Mappers.getMapper(ProductMaterialMapper.class);

    ProductMaterialDTO toDto(ProductMaterial entity);
    ProductMaterial toEntity(ProductMaterialDTO dto);
    void updateEntityFromDto(ProductMaterialDTO dto, @MappingTarget ProductMaterial entity);
}