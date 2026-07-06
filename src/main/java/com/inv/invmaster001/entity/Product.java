package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Product entity mapped from the products table.
 */
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materials = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPriceHistory> priceHistory = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();

    private String productName;

    private String description;

    private Boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // helpers
    public void addMaterial(Material m) {
        materials.add(m);
        m.setProduct(this);
    }

    public void addPriceHistory(ProductPriceHistory h) {
        priceHistory.add(h);
        h.setProduct(this);
    }
}