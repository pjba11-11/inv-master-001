package com.inv.invmaster001.dto.request.product;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRequest {

    private Long id;

    private String materialName;

    private String unit;

    @NotNull
    @Positive
    private BigDecimal currentPrice;
}
