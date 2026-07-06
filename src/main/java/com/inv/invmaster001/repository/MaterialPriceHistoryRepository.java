package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.MaterialPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialPriceHistoryRepository extends JpaRepository<MaterialPriceHistory, Long> {
}