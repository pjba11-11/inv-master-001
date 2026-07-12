package com.inv.invmaster001.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

    private Long invoiceId;

    private String invoiceNumber;

    private LocalDate invoiceDate;

    private BigDecimal subtotal;

    private BigDecimal cgst;

    private BigDecimal sgst;

    private BigDecimal cgstPercentage;

    private BigDecimal sgstPercentage;

    private BigDecimal discount;

    private BigDecimal grandTotal;

    private String status;
    private String createdByName;

    private List<InvoiceLineItemResponse> items;

}