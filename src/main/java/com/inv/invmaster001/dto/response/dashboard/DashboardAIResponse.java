package com.inv.invmaster001.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAIResponse {

    private String businessSummary;

    private String growthPrediction;

    private List<String> recommendations;

    private List<CustomerInsightResponse> customerInsights;

    private List<ProductInsightResponse> productInsights;

    private List<InventorySuggestionResponse> inventorySuggestions;

    private List<PaymentRiskResponse> paymentRisks;

    private ProfitabilityAnalysisResponse profitabilityAnalysis;

    private List<PriceRecommendationResponse> priceRecommendations;

    private DemandForecastResponse demandForecast;

    private CashFlowInsightResponse cashFlowInsights;

    private BusinessScoreResponse overallBusinessScore;

}