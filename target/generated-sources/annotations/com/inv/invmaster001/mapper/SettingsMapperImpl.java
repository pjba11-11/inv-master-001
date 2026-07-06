package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.SettingsDTO;
import com.inv.invmaster001.entity.Settings;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class SettingsMapperImpl implements SettingsMapper {

    @Override
    public SettingsDTO toDto(Settings entity) {
        if ( entity == null ) {
            return null;
        }

        SettingsDTO settingsDTO = new SettingsDTO();

        return settingsDTO;
    }

    @Override
    public Settings toEntity(SettingsDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Settings settings = new Settings();

        return settings;
    }

    @Override
    public void updateEntityFromDto(SettingsDTO dto, Settings entity) {
        if ( dto == null ) {
            return;
        }
    }
}
