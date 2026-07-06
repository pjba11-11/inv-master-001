package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

/**
 * Invoice entity mapped from the invoices table.
 */
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

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    private LocalDate invoiceDate;

    private BigDecimal subtotal;
    private BigDecimal gst;
    private BigDecimal discount;
    private BigDecimal grandTotal;

    private String status;
    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void addLineItem(InvoiceLineItem item) {
        invoiceLineItems.add(item);
        item.setInvoice(this);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setInvoice(this);
    }
}