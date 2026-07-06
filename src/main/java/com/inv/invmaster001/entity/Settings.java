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
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "company_id", unique = true)
    private Company company;

    @Column(name = "gst_percentage", precision = 5, scale = 2)
    private BigDecimal gstPercentage;

    @Column(name = "default_profit_margin", precision = 5, scale = 2)
    private BigDecimal defaultProfitMargin;

    private String currency;
    @Column(name = "invoice_prefix")
    private String invoicePrefix;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}