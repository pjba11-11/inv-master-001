package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.InvoiceSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceSequenceRepository extends JpaRepository<InvoiceSequence, Long> {

    List<InvoiceSequence> findByCompanyIdAndDeletedAtIsNull(
            Long companyId
    );

    List<InvoiceSequence> findByCompanyIdAndInvoiceDateBetweenAndDeletedAtIsNull(
            Long companyId,
            LocalDate from,
            LocalDate to
    );

}