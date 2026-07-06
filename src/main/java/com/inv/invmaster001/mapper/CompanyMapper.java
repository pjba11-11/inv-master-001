package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.CompanyDTO;
import com.inv.invmaster001.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDTO toDto(Company entity);
    Company toEntity(CompanyDTO dto);
    void updateEntityFromDto(CompanyDTO dto, @MappingTarget Company entity);
}