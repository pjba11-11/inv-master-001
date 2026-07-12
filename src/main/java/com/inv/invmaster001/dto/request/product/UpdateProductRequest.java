package com.inv.invmaster001.dto.request.product;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {

    @NotBlank
    private String productName;

    private String description;

    @NotNull
    @PositiveOrZero
    private BigDecimal labourCharges;

    @NotNull
    @PositiveOrZero
    private BigDecimal profitMargin;

    private String hsnCode;

    @NotEmpty
    private List<Long> materialIds;
}
