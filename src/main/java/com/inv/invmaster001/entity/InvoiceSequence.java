package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing the invoice_sequences table.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invoice_sequences", uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "year"}))
public class InvoiceSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "year")
    private int year;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "next_number")
    private int nextNumber;
}