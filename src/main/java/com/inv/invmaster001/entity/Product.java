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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    private List<Material> materials = new ArrayList<>();


    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    private List<ProductPriceHistory> priceHistory = new ArrayList<>();


    @Column(name = "product_name")
    private String productName;


    private String description;


    private Boolean active = true;

    @Column(name = "hsn_code", length = 20)
    private String hsnCode;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    private LocalDateTime deletedAt;


    // ============================
    // HELPERS
    // ============================

    public void addMaterial(Material material) {

        materials.add(material);
        material.setProduct(this);

    }


    public void addPriceHistory(ProductPriceHistory history) {

        priceHistory.add(history);
        history.setProduct(this);

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

