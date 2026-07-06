package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.AnalyticsCacheDTO;
import com.inv.invmaster001.entity.AnalyticsCache;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnalyticsCacheMapper {

    AnalyticsCacheMapper INSTANCE = Mappers.getMapper(AnalyticsCacheMapper.class);

    AnalyticsCacheDTO toDto(AnalyticsCache entity);
    AnalyticsCache toEntity(AnalyticsCacheDTO dto);
    void updateEntityFromDto(AnalyticsCacheDTO dto, @MappingTarget AnalyticsCache entity);
}