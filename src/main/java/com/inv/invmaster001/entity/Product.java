package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;

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


    @OneToMany(mappedBy = "product")
    private List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();


    @Column(name = "product_name")
    private String productName;


    private String description;


    private Boolean active = true;


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

