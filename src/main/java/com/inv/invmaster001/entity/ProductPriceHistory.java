package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_price_history")
public class ProductPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "manufacturing_cost", precision = 12, scale = 2)
    private BigDecimal manufacturingCost;

    @Column(name = "selling_price", precision = 12, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "profit_margin", precision = 5, scale = 2)
    private BigDecimal profitMargin;

    @Column(name = "effective_from")
    private java.sql.Date effectiveFrom;

    @Column(name = "effective_to")
    private java.sql.Date effectiveTo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}