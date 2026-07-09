package com.inv.invmaster001.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "invoice_line_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceLineItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;



    @Column(name = "product_id")
    private Long productId;



    private String productName;

    private String hsnCode;

    @Column(
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal quantity;



    @Column(
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal unitPrice;



    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;



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