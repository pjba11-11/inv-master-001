package com.inv.invmaster001.dto.entitydto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaterialDTO {
    private Long id;
    private Long companyId;
    private String materialName;
    private String unit;
    private BigDecimal currentPrice;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}