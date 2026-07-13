package com.inv.invmaster001.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRiskResponse {

    private Long customerId;

    private String customerName;

    private String riskLevel;

    private String reason;

}