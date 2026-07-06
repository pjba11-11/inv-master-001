package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.ProductPriceHistoryDTO;
import com.inv.invmaster001.entity.ProductPriceHistory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductPriceHistoryMapper {

    ProductPriceHistoryMapper INSTANCE = Mappers.getMapper(ProductPriceHistoryMapper.class);

    ProductPriceHistoryDTO toDto(ProductPriceHistory entity);
    ProductPriceHistory toEntity(ProductPriceHistoryDTO dto);
    void updateEntityFromDto(ProductPriceHistoryDTO dto, @MappingTarget ProductPriceHistory entity);
}