package com.inv.invmaster001.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDocumentItem {

    private Integer serialNumber;

    private String productName;

    private String hsnCode;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}