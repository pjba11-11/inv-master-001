package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inv.invmaster001.entity.ProductPriceHistory;

import java.util.Optional;

public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory, Long> {

    Optional<ProductPriceHistory> findFirstByProductOrderByEffectiveFromDesc(Product product);

}