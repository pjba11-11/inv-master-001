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
public class DashboardPeriodResponse {

    private DashboardPeriod period;

    // ==========================
    // KPI CARDS
    // ==========================

    private BigDecimal revenue;

    private BigDecimal collectedAmount;

    private BigDecimal outstandingAmount;

    private Integer totalInvoices;

    private Integer totalCustomers;

    private BigDecimal averageInvoiceValue;

    private BigDecimal growthPercentage;

    // ==========================
    // CHARTS
    // ==========================

    private List<MonthlyRevenueResponse> monthlyRevenue;

    private List<MonthlyInvoiceCountResponse> monthlyInvoiceCount;

    // ==========================
    // TABLES
    // ==========================

    private List<TopProductResponse> topProducts;

    private List<TopCustomerResponse> topCustomers;

    // ==========================
    // AI (Future)
    // ==========================

    private GrowthPredictionResponse growthPrediction;

}