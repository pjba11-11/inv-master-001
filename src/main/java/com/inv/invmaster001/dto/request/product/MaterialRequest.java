package com.inv.invmaster001.dto.request.product;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRequest {

    private Long id;

    private String materialName;

    private String hsnCode;

    private String unit;

    @NotNull
    @Positive
    private BigDecimal currentPrice;
}
