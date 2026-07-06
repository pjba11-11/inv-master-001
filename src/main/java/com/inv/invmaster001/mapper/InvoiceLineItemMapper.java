package com.inv.invmaster001.mapper;


import com.inv.invmaster001.dto.entitydto.InvoiceLineItemDTO;
import com.inv.invmaster001.entity.InvoiceLineItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvoiceLineItemMapper {

    InvoiceLineItemMapper INSTANCE = Mappers.getMapper(InvoiceLineItemMapper.class);

    InvoiceLineItemDTO toDto(InvoiceLineItem entity);
    InvoiceLineItem    toEntity(InvoiceLineItemDTO dto);
    void               updateEntityFromDto(InvoiceLineItemDTO dto, @MappingTarget InvoiceLineItem entity);
}