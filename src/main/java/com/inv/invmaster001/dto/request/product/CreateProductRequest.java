package com.inv.invmaster001.dto.request.product;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    private Long companyId;

    @NotBlank
    private String productName;

    private String description;

    private BigDecimal manufacturingCost;

    private BigDecimal sellingPrice;

    @Valid
    @NotEmpty
    private List<MaterialRequest> materials;
}