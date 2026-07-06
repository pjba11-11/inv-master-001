package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.ProductDTO;
import com.inv.invmaster001.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDto(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        return productDTO;
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        return product;
    }

    @Override
    public void updateEntityFromDto(ProductDTO dto, Product entity) {
        if ( dto == null ) {
            return;
        }
    }
}
