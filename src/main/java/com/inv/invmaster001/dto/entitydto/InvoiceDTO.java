package com.inv.invmaster001.dto.entitydto;

import lombok.Data;
import java.math.BigDecimal;

import java.time.LocalDateTime;

@Data
public class InvoiceDTO {
    private Long id;
    private String invoiceNumber;
    private Long companyId;
    private Long customerId;
    private Long createdById;
    private java.sql.Date invoiceDate;
    private BigDecimal subtotal;
    private BigDecimal gst;
    private BigDecimal discount;
    private BigDecimal grandTotal;
    private String status;
    private String remarks;

    // For nested relationships (optional, can be populated as needed)
    // private CustomerDTO customer;
    // private List<InvoiceLineItemDTO> lineItems;
    // private List<PaymentDTO> payments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}