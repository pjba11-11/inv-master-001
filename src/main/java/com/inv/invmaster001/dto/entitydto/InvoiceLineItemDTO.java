package com.inv.invmaster001.dto.entitydto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InvoiceLineItemDTO {
    private Long id;
    private Long invoiceId;
    private Long productId;
    private String productName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
}