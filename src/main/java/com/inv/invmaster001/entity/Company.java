package com.inv.invmaster001.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Company entity mapped from the companies table.
 */
@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Settings settings;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Invoice> invoices = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Customer> customers = new ArrayList<>();


    // =========================
    // FIELDS
    // =========================

    @Column(nullable = false, length = 150)
    private String companyName;


    @Column(name = "gst_number", length = 30)
    private String gstNumber;


    @Column(length = 20)
    private String phone;


    @Column(length = 255)
    private String email;


    @Column(columnDefinition = "TEXT")
    private String address;


    @Column(name = "bank_name", length = 100)
    private String bankName;


    @Column(name = "account_number", length = 50)
    private String accountNumber;


    @Column(length = 20)
    private String ifsc;


    @Column(name = "upi_id", length = 50)
    private String upiId;


    @Column(name = "logo", columnDefinition = "TEXT")
    private String logo;



    @Column(updatable = false)
    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    private LocalDateTime deletedAt;



    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

    }



    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();

    }



    public void addUser(User user) {

        users.add(user);

        user.setCompany(this);

    }



    public void addProduct(Product product) {

        products.add(product);

        product.setCompany(this);

    }



    public void addInvoice(Invoice invoice) {

        invoices.add(invoice);

        invoice.setCompany(this);

    }



    public void addCustomer(Customer customer) {

        customers.add(customer);

        customer.setCompany(this);

    }



    public void setSettings(Settings settings) {

        if (this.settings != null) {

            this.settings.setCompany(null);

        }

        this.settings = settings;

        if (settings != null) {

            settings.setCompany(this);

        }

    }

}