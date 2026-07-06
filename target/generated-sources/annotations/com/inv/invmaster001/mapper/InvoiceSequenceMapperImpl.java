package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.InvoiceSequenceDTO;
import com.inv.invmaster001.entity.InvoiceSequence;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class InvoiceSequenceMapperImpl implements InvoiceSequenceMapper {

    @Override
    public InvoiceSequenceDTO toDto(InvoiceSequence entity) {
        if ( entity == null ) {
            return null;
        }

        InvoiceSequenceDTO invoiceSequenceDTO = new InvoiceSequenceDTO();

        return invoiceSequenceDTO;
    }

    @Override
    public InvoiceSequence toEntity(InvoiceSequenceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        InvoiceSequence invoiceSequence = new InvoiceSequence();

        return invoiceSequence;
    }

    @Override
    public void updateEntityFromDto(InvoiceSequenceDTO dto, InvoiceSequence entity) {
        if ( dto == null ) {
            return;
        }
    }
}
