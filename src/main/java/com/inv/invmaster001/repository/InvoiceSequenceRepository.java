package com.inv.invmaster001.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inv.invmaster001.entity.InvoiceSequence;

@Repository
public interface InvoiceSequenceRepository extends JpaRepository<InvoiceSequence, Long> {
}