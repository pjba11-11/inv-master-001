package com.inv.invmaster001.dto.entitydto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Long id;
    private Long companyId;
    private String productName;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // For nested relationships (optional, can be populated as needed)
    // private List<ProductMaterialDTO> productMaterials;
    // private List<ProductPriceHistoryDTO> priceHistory;
}