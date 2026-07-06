package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inv.invmaster001.entity.MaterialPriceHistory;

import java.util.Optional;

public interface MaterialPriceHistoryRepository extends JpaRepository<MaterialPriceHistory, Long> {

    Optional<MaterialPriceHistory> findFirstByMaterialOrderByEffectiveFromDesc(Material material);

}