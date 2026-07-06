package com.inv.invmaster001.dto.entitydto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaterialPriceHistoryDTO {
    private Long id;
    private Long materialId;
    private BigDecimal price;
    private java.sql.Date effectiveFrom;
    private java.sql.Date effectiveTo;
    private LocalDateTime createdAt;
}