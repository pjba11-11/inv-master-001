package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.AnalyticsCacheDTO;
import com.inv.invmaster001.entity.AnalyticsCache;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class AnalyticsCacheMapperImpl implements AnalyticsCacheMapper {

    @Override
    public AnalyticsCacheDTO toDto(AnalyticsCache entity) {
        if ( entity == null ) {
            return null;
        }

        AnalyticsCacheDTO analyticsCacheDTO = new AnalyticsCacheDTO();

        return analyticsCacheDTO;
    }

    @Override
    public AnalyticsCache toEntity(AnalyticsCacheDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AnalyticsCache analyticsCache = new AnalyticsCache();

        return analyticsCache;
    }

    @Override
    public void updateEntityFromDto(AnalyticsCacheDTO dto, AnalyticsCache entity) {
        if ( dto == null ) {
            return;
        }
    }
}
