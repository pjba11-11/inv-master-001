package com.inv.invmaster001.dto.request.invoice;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreatePaymentRequest {

    @NotNull
    private LocalDate paymentDate;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private String paymentMethod;

    private String transactionReference;

    private String remarks;
}
