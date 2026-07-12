package com.inv.invmaster001.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFullResponse {

    private Long productId;
    private String productName;
    private String description;
    private Boolean active;
    private String hsnCode;
    private BigDecimal manufacturingCost;
    private BigDecimal labourCharges;
    private BigDecimal sellingPrice;
    private BigDecimal profitMargin;

    private List<MaterialResponse> materials;
    private String createdByName;
}