package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.MaterialPriceHistoryDTO;
import com.inv.invmaster001.entity.MaterialPriceHistory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MaterialPriceHistoryMapper {

    MaterialPriceHistoryMapper INSTANCE = Mappers.getMapper(MaterialPriceHistoryMapper.class);

    MaterialPriceHistoryDTO toDto(MaterialPriceHistory entity);
    MaterialPriceHistory toEntity(MaterialPriceHistoryDTO dto);
    void updateEntityFromDto(MaterialPriceHistoryDTO dto, @MappingTarget MaterialPriceHistory entity);
}