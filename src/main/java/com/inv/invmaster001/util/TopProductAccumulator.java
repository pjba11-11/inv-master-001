package com.inv.invmaster001.util;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopProductAccumulator {

    private final String productName;

    private BigDecimal quantity = BigDecimal.ZERO;

    private BigDecimal revenue = BigDecimal.ZERO;

    public TopProductAccumulator(String productName) {
        this.productName = productName;
    }
}