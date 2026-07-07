package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCompanyId(Long companyId);


    boolean existsByCompanyIdAndProductNameIgnoreCase(
            Long companyId,
            String productName
    );

}