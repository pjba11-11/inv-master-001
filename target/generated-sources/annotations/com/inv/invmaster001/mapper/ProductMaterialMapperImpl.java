package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.ProductMaterialDTO;
import com.inv.invmaster001.entity.ProductMaterial;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class ProductMaterialMapperImpl implements ProductMaterialMapper {

    @Override
    public ProductMaterialDTO toDto(ProductMaterial entity) {
        if ( entity == null ) {
            return null;
        }

        ProductMaterialDTO productMaterialDTO = new ProductMaterialDTO();

        return productMaterialDTO;
    }

    @Override
    public ProductMaterial toEntity(ProductMaterialDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductMaterial productMaterial = new ProductMaterial();

        return productMaterial;
    }

    @Override
    public void updateEntityFromDto(ProductMaterialDTO dto, ProductMaterial entity) {
        if ( dto == null ) {
            return;
        }
    }
}
