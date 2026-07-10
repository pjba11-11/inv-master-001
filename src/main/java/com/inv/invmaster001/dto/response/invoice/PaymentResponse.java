package com.inv.invmaster001.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long invoiceId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionReference;
    private String remarks;
    private LocalDateTime createdAt;
}
