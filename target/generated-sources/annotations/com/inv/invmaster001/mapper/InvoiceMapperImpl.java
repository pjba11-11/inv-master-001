package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.InvoiceDTO;
import com.inv.invmaster001.entity.Invoice;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T17:03:48+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class InvoiceMapperImpl implements InvoiceMapper {

    @Override
    public InvoiceDTO toDto(Invoice entity) {
        if ( entity == null ) {
            return null;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();

        invoiceDTO.setId( entity.getId() );
        invoiceDTO.setInvoiceNumber( entity.getInvoiceNumber() );
        invoiceDTO.setInvoiceDate( entity.getInvoiceDate() );
        invoiceDTO.setSubtotal( entity.getSubtotal() );
        invoiceDTO.setGst( entity.getGst() );
        invoiceDTO.setDiscount( entity.getDiscount() );
        invoiceDTO.setGrandTotal( entity.getGrandTotal() );
        invoiceDTO.setStatus( entity.getStatus() );
        invoiceDTO.setRemarks( entity.getRemarks() );
        invoiceDTO.setCreatedAt( entity.getCreatedAt() );
        invoiceDTO.setUpdatedAt( entity.getUpdatedAt() );

        return invoiceDTO;
    }

    @Override
    public Invoice toEntity(InvoiceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Invoice.InvoiceBuilder invoice = Invoice.builder();

        invoice.id( dto.getId() );
        invoice.invoiceNumber( dto.getInvoiceNumber() );
        invoice.invoiceDate( dto.getInvoiceDate() );
        invoice.subtotal( dto.getSubtotal() );
        invoice.gst( dto.getGst() );
        invoice.discount( dto.getDiscount() );
        invoice.grandTotal( dto.getGrandTotal() );
        invoice.status( dto.getStatus() );
        invoice.remarks( dto.getRemarks() );
        invoice.createdAt( dto.getCreatedAt() );
        invoice.updatedAt( dto.getUpdatedAt() );

        return invoice.build();
    }

    @Override
    public void updateEntityFromDto(InvoiceDTO dto, Invoice entity) {
        if ( dto == null ) {
            return;
        }

        entity.setId( dto.getId() );
        entity.setInvoiceNumber( dto.getInvoiceNumber() );
        entity.setInvoiceDate( dto.getInvoiceDate() );
        entity.setSubtotal( dto.getSubtotal() );
        entity.setGst( dto.getGst() );
        entity.setDiscount( dto.getDiscount() );
        entity.setGrandTotal( dto.getGrandTotal() );
        entity.setStatus( dto.getStatus() );
        entity.setRemarks( dto.getRemarks() );
        entity.setCreatedAt( dto.getCreatedAt() );
        entity.setUpdatedAt( dto.getUpdatedAt() );
    }
}
