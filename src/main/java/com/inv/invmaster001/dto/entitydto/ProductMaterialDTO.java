package com.inv.invmaster001.dto.entitydto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductMaterialDTO {
    private Long id;
    private Long productId;
    private Long materialId;
    private BigDecimal quantity;

    // For nested objects (optional)
    // private ProductDTO product;
    // private MaterialDTO material;
}