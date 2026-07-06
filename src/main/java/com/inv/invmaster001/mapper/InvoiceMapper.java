package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.InvoiceDTO;
import com.inv.invmaster001.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    InvoiceDTO toDto(Invoice entity);
    Invoice    toEntity(InvoiceDTO dto);
    void       updateEntityFromDto(InvoiceDTO dto, @MappingTarget Invoice entity);
}
