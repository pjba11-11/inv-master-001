package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

/**
 * Material entity mapped from the materials table.
 */
@Entity
@Table(name = "materials")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialPriceHistory> priceHistory = new ArrayList<>();

    @Column(name = "material_name", length = 100, nullable = false)
    private String materialName;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "current_price", precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // helper methods to keep both sides in sync
    public void addPriceHistory(MaterialPriceHistory history) {
        priceHistory.add(history);
        history.setMaterial(this);
    }

    public void removePriceHistory(MaterialPriceHistory history) {
        priceHistory.remove(history);
        history.setMaterial(null);
    }
}