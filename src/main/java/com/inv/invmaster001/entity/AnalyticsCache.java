package com.inv.invmaster001.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "analytics_cache")
public class AnalyticsCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "analysis_type")
    private String analysisType;

    @Column(name = "period_start")
    private java.sql.Date periodStart;

    @Column(name = "period_end")
    private java.sql.Date periodEnd;

    @Column(name = "analysis_json", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private JsonNode analysisJson;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}