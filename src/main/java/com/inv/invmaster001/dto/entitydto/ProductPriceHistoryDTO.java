package com.inv.invmaster001.dto.entitydto;

import lombok.Data;
import java.math.BigDecimal;

import java.time.LocalDateTime;

@Data
public class ProductPriceHistoryDTO {
    private Long id;
    private Long productId;
    private BigDecimal manufacturingCost;
    private BigDecimal sellingPrice;
    private BigDecimal profitMargin;
    private java.sql.Date effectiveFrom;
    private java.sql.Date effectiveTo;
    private LocalDateTime createdAt;
}