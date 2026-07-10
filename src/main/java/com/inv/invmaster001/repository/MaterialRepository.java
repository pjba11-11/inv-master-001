package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    List<Material> findByCompanyIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long companyId);

    Optional<Material> findByIdAndCompanyIdAndDeletedAtIsNull(Long id, Long companyId);

    Optional<Material> findByIdAndDeletedAtIsNull(Long id);
}
