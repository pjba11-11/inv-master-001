package com.inv.invmaster001.dto.entitydto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private Long invoiceId;
    private java.sql.Date paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionReference;
    private String remarks;
    private LocalDateTime createdAt;
}