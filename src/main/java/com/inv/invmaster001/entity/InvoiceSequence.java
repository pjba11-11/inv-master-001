package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "invoice_sequences",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_company_invoice_number",
                        columnNames = {
                                "company_id",
                                "invoice_number"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    // Stored reference only - no FK
    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;


    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;


    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}