package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invoices", uniqueConstraints = @UniqueConstraint(columnNames = "invoice_number"))
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "invoice_date")
    private java.sql.Date invoiceDate;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "gst", precision = 12, scale = 2)
    private BigDecimal gst;

    @Column(name = "discount", precision = 12, scale = 2)
    private BigDecimal discount;

    @Column(name = "grand_total", precision = 12, scale = 2)
    private BigDecimal grandTotal;

    @Column(name = "status", length = 30, columnDefinition = "VARCHAR(30) DEFAULT 'GENERATED'")
    private String status;

    private String remarks;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceLineItem> lineItems = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}