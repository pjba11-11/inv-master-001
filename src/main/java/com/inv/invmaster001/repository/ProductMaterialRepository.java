package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {
}