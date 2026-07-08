package com.inv.invmaster001.dto.request.invoice;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequest {

    @NotNull
    private Long customerId;

    private List<InvoiceItemRequest> items;

    private BigDecimal discount;

    private String remarks;

}