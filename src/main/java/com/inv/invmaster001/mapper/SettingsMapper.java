package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.SettingsDTO;
import com.inv.invmaster001.entity.Settings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SettingsMapper {

    SettingsMapper INSTANCE = Mappers.getMapper(SettingsMapper.class);

    SettingsDTO toDto(Settings entity);
    Settings toEntity(SettingsDTO dto);
    void updateEntityFromDto(SettingsDTO dto, @MappingTarget Settings entity);
}