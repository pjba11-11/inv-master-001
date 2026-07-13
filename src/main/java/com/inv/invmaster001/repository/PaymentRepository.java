package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Find by invoice
    java.util.List<Payment> findByInvoiceId(Long invoiceId);

    List<Payment> findByInvoiceIdIn(List<Long> invoiceIds);
    }