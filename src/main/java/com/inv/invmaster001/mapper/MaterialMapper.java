package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.MaterialDTO;
import com.inv.invmaster001.entity.Material;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);

    MaterialDTO toDto(Material entity);
    Material toEntity(MaterialDTO dto);
    void updateEntityFromDto(MaterialDTO dto, @MappingTarget Material entity);
}