package com.inv.invmaster001.dto.request.product;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class CreateProductRequest {

    @NotBlank
    private String productName;

    private String description;

    private BigDecimal manufacturingCost;

    private BigDecimal sellingPrice;

    private String hsnCode;

    @Valid
    @NotEmpty
    private List<MaterialRequest> materials;
}