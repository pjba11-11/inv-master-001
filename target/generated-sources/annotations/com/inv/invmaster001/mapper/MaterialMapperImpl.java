package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.MaterialDTO;
import com.inv.invmaster001.entity.Material;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class MaterialMapperImpl implements MaterialMapper {

    @Override
    public MaterialDTO toDto(Material entity) {
        if ( entity == null ) {
            return null;
        }

        MaterialDTO materialDTO = new MaterialDTO();

        return materialDTO;
    }

    @Override
    public Material toEntity(MaterialDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Material material = new Material();

        return material;
    }

    @Override
    public void updateEntityFromDto(MaterialDTO dto, Material entity) {
        if ( dto == null ) {
            return;
        }
    }
}
