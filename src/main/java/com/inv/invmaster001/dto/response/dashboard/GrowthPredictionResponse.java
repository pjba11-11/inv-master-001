package com.inv.invmaster001.dto.response.dashboard;

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
public class GrowthPredictionResponse {

    private String companyHealth;

    private BigDecimal expectedGrowthPercentage;

    private BigDecimal expectedRevenue;

    private String summary;

    private List<String> strengths;

    private List<String> risks;

    private List<String> recommendations;

}