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

    private BigDecimal revenue;

    private Integer totalInvoices;

    private Integer totalCustomers;

    private BigDecimal averageInvoiceValue;

    private BigDecimal collectedAmount;

    private BigDecimal outstandingAmount;

    private BigDecimal growthPercentage;

    private List<TopProductResponse> topProducts;

    private List<TopCustomerResponse> topCustomers;

    private GrowthPredictionResponse growthPrediction;
}