package com.inv.invmaster001.util;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopCustomerAccumulator {

    private final String customerName;

    private Integer invoiceCount = 0;

    private BigDecimal revenue = BigDecimal.ZERO;

    public TopCustomerAccumulator(String customerName) {
        this.customerName = customerName;
    }
}