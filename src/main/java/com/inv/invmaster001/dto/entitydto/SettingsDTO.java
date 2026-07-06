package com.inv.invmaster001.dto.entitydto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SettingsDTO {
    private Long id;
    private Long companyId;
    private BigDecimal gstPercentage;
    private BigDecimal defaultProfitMargin;
    private String currency;
    private String invoicePrefix;
    private String financialYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}