package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(unique = true, nullable = false)
    private String invoiceNumber;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<InvoiceLineItem> invoiceLineItems =
            new ArrayList<>();



    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Payment> payments =
            new ArrayList<>();

    private String poNumber;

    private LocalDate invoiceDate;


    private BigDecimal subtotal;


    @Column(name = "cgst", precision = 12, scale = 2, nullable = false)
    private BigDecimal cgst;


    @Column(name = "sgst", precision = 12, scale = 2, nullable = false)
    private BigDecimal sgst;


    @Column(name = "cgst_percentage", precision = 5, scale = 2)
    private BigDecimal cgstPercentage;


    @Column(name = "sgst_percentage", precision = 5, scale = 2)
    private BigDecimal sgstPercentage;


    private BigDecimal discount;


    private BigDecimal grandTotal;


    @Column(name = "grand_total_words", nullable = false)
    private String grandTotalWords;

    private String status;


    private String remarks;



    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    // ===============================
    // Bidirectional helpers
    // ===============================


    public void addLineItem(InvoiceLineItem item) {

        invoiceLineItems.add(item);

        item.setInvoice(this);

    }



    public void removeLineItem(InvoiceLineItem item) {

        invoiceLineItems.remove(item);

        item.setInvoice(null);

    }



    public void addPayment(Payment payment) {

        payments.add(payment);

        payment.setInvoice(this);

    }



    // ===============================
    // Audit
    // ===============================


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