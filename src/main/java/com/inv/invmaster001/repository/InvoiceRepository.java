package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByCompanyIdAndDeletedAtIsNullOrderByInvoiceDateDesc(Long companyId);

    Optional<Invoice> findByIdAndCompanyIdAndDeletedAtIsNull(Long id, Long companyId);
}
