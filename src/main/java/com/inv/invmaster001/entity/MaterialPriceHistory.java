package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * MaterialPriceHistory entity mapped from the material_price_history table.
 */
@Entity
@Table(name = "material_price_history")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialPriceHistory {


    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;


    @Column(name = "price", precision = 12, scale = 2, nullable = false)
    private BigDecimal price;


    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;


    @Column(name = "effective_to")
    private LocalDate effectiveTo;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;



    // ============================
    // AUDIT
    // ============================

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

    }


    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();

    }
}

