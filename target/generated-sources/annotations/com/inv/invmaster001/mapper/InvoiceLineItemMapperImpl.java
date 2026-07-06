package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.InvoiceLineItemDTO;
import com.inv.invmaster001.entity.InvoiceLineItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class InvoiceLineItemMapperImpl implements InvoiceLineItemMapper {

    @Override
    public InvoiceLineItemDTO toDto(InvoiceLineItem entity) {
        if ( entity == null ) {
            return null;
        }

        InvoiceLineItemDTO invoiceLineItemDTO = new InvoiceLineItemDTO();

        return invoiceLineItemDTO;
    }

    @Override
    public InvoiceLineItem toEntity(InvoiceLineItemDTO dto) {
        if ( dto == null ) {
            return null;
        }

        InvoiceLineItem invoiceLineItem = new InvoiceLineItem();

        return invoiceLineItem;
    }

    @Override
    public void updateEntityFromDto(InvoiceLineItemDTO dto, InvoiceLineItem entity) {
        if ( dto == null ) {
            return;
        }
    }
}
