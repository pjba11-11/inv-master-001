package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.InvoiceLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceLineItemRepository extends JpaRepository<InvoiceLineItem, Long> {
}