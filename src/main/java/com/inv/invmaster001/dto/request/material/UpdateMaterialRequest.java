package com.inv.invmaster001.dto.request.material;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateMaterialRequest {

    private String materialName;
    private String unit;
    private String hsnCode;
    private BigDecimal currentPrice;
    private Boolean active;
}
