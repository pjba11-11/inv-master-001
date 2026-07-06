package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.InvoiceSequenceDTO;
import com.inv.invmaster001.entity.InvoiceSequence;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvoiceSequenceMapper {

    InvoiceSequenceMapper INSTANCE = Mappers.getMapper(InvoiceSequenceMapper.class);

    InvoiceSequenceDTO toDto(InvoiceSequence entity);
    InvoiceSequence toEntity(InvoiceSequenceDTO dto);
    void updateEntityFromDto(InvoiceSequenceDTO dto, @MappingTarget InvoiceSequence entity);
}