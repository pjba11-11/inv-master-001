package com.inv.invmaster001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * Customer entity mapped from the customers table.
 */
@Entity
@Table(name = "customers")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // =========================
    // RELATIONSHIPS
    // =========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "company_id",
            nullable = false
    )
    private Company company;



    // =========================
    // FIELDS
    // =========================

    @Column(
            name = "customer_name",
            nullable = false,
            length = 150
    )
    private String customerName;


    @Column(
            name = "gst_number",
            length = 30
    )
    private String gstNumber;


    @Column(length = 20)
    private String phone;


    @Column(length = 255)
    private String email;


    @Column(columnDefinition = "TEXT")
    private String address;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @Column(
            name = "bank_name",
            length = 100
    )
    private String bankName;


    @Column(
            name = "account_number",
            length = 50
    )
    private String accountNumber;


    @Column(length = 20)
    private String ifsc;


    @Column(
            name = "upi_id",
            length = 50
    )
    private String upiId;



    // =========================
    // AUDIT FIELDS
    // =========================

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;



    // =========================
    // AUDIT CALLBACKS
    // =========================

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
