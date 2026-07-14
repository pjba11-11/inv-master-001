package com.inv.invmaster001.repository;

import com.inv.invmaster001.entity.AnalyticsCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalyticsCacheRepository extends JpaRepository<AnalyticsCache, Long> {

    Optional<AnalyticsCache> findByCompanyIdAndAnalysisType(Long companyId, String analysisType);
}