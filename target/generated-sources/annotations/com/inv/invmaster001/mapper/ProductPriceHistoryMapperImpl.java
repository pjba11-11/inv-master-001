package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.ProductPriceHistoryDTO;
import com.inv.invmaster001.entity.ProductPriceHistory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class ProductPriceHistoryMapperImpl implements ProductPriceHistoryMapper {

    @Override
    public ProductPriceHistoryDTO toDto(ProductPriceHistory entity) {
        if ( entity == null ) {
            return null;
        }

        ProductPriceHistoryDTO productPriceHistoryDTO = new ProductPriceHistoryDTO();

        return productPriceHistoryDTO;
    }

    @Override
    public ProductPriceHistory toEntity(ProductPriceHistoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductPriceHistory productPriceHistory = new ProductPriceHistory();

        return productPriceHistory;
    }

    @Override
    public void updateEntityFromDto(ProductPriceHistoryDTO dto, ProductPriceHistory entity) {
        if ( dto == null ) {
            return;
        }
    }
}
