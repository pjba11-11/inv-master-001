package com.inv.invmaster001.dto.request.material;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateMaterialRequest {

    @NotBlank
    private String materialName;

    @NotBlank
    private String unit;

    private String hsnCode;

    @NotNull
    @Positive
    private BigDecimal currentPrice;

    private Boolean active = true;
}
