package com.inv.invmaster001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "company_id",
            nullable = false,
            unique = true
    )
    private Company company;


    @Column(name = "gst_percentage", precision = 5, scale = 2)
    private BigDecimal gstPercentage;


    @Column(name = "cgst_percentage", precision = 5, scale = 2)
    private BigDecimal cgstPercentage;


    @Column(name = "sgst_percentage", precision = 5, scale = 2)
    private BigDecimal sgstPercentage;

    @Column(
            name = "vehicle_numbers",
            columnDefinition = "text[]"
    )
    private String[] vehicleNumbers;

    @Column(name = "default_profit_margin", precision = 5, scale = 2)
    private BigDecimal defaultProfitMargin;


    @Column(name = "currency", length = 10)
    private String currency;


    @Column(name = "invoice_prefix", length = 20)
    private String invoicePrefix;


    @Column(name = "financial_year", length = 20)
    private String financialYear;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}