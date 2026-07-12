package com.inv.invmaster001.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(
            mappedBy = "material",
            cascade = CascadeType.ALL
    )
    private List<MaterialPriceHistory> priceHistory = new ArrayList<>();


    @Column(name = "material_name", length = 100, nullable = false)
    private String materialName;




    @Column(name = "hsn_code", length = 20)
    private String hsnCode;

    @Column(name = "unit", length = 20)
    private String unit;


    @Column(name = "current_price", precision = 12, scale = 2)
    private BigDecimal currentPrice;


    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    // ============================
    // PRICE HISTORY HELPER
    // ============================

    public void addPriceHistory(MaterialPriceHistory history) {

        priceHistory.add(history);
        history.setMaterial(this);

    }


    public void removePriceHistory(MaterialPriceHistory history) {

        priceHistory.remove(history);
        history.setMaterial(null);

    }


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

