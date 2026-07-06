package com.inv.invmaster001.dto.entitydto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalyticsCacheDTO {
    private Long id;
    private Long companyId;
    private String analysisType;
    private java.sql.Date periodStart;
    private java.sql.Date periodEnd;
    private String analysisJson;
    private LocalDateTime generatedAt;
}