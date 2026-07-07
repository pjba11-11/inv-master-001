package com.inv.invmaster001.dto.request.settings;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSettingsRequest {

    private BigDecimal gstPercentage;

    private BigDecimal cgstPercentage;

    private BigDecimal sgstPercentage;

    private String[] vehicleNumbers;

    private BigDecimal defaultProfitMargin;

    private String currency;

    private String invoicePrefix;

    private String financialYear;

}