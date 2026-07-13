package com.inv.invmaster001.dto.response.dashboard;

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
public class GrowthPredictionResponse {

    /**
     * Predicted revenue for the next period.
     */
    private BigDecimal predictedRevenue;

    /**
     * Expected growth percentage.
     * Example: 12.50 means +12.5%
     */
    private BigDecimal predictedGrowthPercentage;

    /**
     * AI-generated summary.
     */
    private String summary;

    /**
     * AI-generated recommendations.
     */
    private String recommendations;
}