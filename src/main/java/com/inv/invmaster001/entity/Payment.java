package com.inv.invmaster001.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Column(name = "payment_date")
    private java.sql.Date paymentDate;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    private String paymentMethod;
    private String transactionReference;
    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}