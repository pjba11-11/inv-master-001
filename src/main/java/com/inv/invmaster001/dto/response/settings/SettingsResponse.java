package com.inv.invmaster001.dto.response.settings;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SettingsResponse {

    private Long id;

    private BigDecimal gstPercentage;

    private BigDecimal cgstPercentage;

    private BigDecimal sgstPercentage;

    private String[] vehicleNumbers;

    private BigDecimal defaultProfitMargin;

    private String currency;

    private String invoicePrefix;

    private String financialYear;

}