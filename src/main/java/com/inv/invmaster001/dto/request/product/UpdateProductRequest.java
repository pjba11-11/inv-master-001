package com.inv.invmaster001.dto.request.product;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    private Boolean active;

    private BigDecimal manufacturingCost;

    private BigDecimal sellingPrice;

    private String hsnCode;

    @Valid
    private List<MaterialRequest> materials;
}
