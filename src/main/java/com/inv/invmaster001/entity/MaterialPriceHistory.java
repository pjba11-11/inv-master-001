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
@Table(name = "material_price_history")
public class MaterialPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "effective_from")
    private java.sql.Date effectiveFrom;

    @Column(name = "effective_to")
    private java.sql.Date effectiveTo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}