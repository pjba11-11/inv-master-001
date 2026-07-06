package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.MaterialPriceHistoryDTO;
import com.inv.invmaster001.entity.MaterialPriceHistory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class MaterialPriceHistoryMapperImpl implements MaterialPriceHistoryMapper {

    @Override
    public MaterialPriceHistoryDTO toDto(MaterialPriceHistory entity) {
        if ( entity == null ) {
            return null;
        }

        MaterialPriceHistoryDTO materialPriceHistoryDTO = new MaterialPriceHistoryDTO();

        return materialPriceHistoryDTO;
    }

    @Override
    public MaterialPriceHistory toEntity(MaterialPriceHistoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MaterialPriceHistory materialPriceHistory = new MaterialPriceHistory();

        return materialPriceHistory;
    }

    @Override
    public void updateEntityFromDto(MaterialPriceHistoryDTO dto, MaterialPriceHistory entity) {
        if ( dto == null ) {
            return;
        }
    }
}
