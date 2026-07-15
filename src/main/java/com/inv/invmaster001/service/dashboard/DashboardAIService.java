package com.inv.invmaster001.service.dashboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inv.invmaster001.dto.response.dashboard.DashboardAIResponse;
import com.inv.invmaster001.dto.response.dashboard.DashboardResponse;
import com.inv.invmaster001.entity.AnalyticsCache;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.exception.RateLimitExceededException;
import com.inv.invmaster001.repository.AnalyticsCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DashboardAIService {

    private static final String ANALYSIS_TYPE = "DASHBOARD_AI";
    private static final long CACHE_TTL_MINUTES = 30;

    /**
     * Short cooldown to collapse accidental double-submits / concurrent
     * duplicate requests (e.g. double-click, multiple tabs) that could
     * otherwise both race past the cache check and each fire a real LLM
     * call. This is independent of, and much shorter than, the 30-minute
     * cache above, which remains the primary cost control.
     */
    private static final Duration LLM_CALL_COOLDOWN = Duration.ofSeconds(5);

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final AnalyticsCacheRepository analyticsCacheRepository;

    private final Map<Long, Instant> lastLlmCallByCompanyId = new ConcurrentHashMap<>();

    public DashboardAIResponse generateInsights(
            DashboardResponse dashboardResponse,
            Company company) {

        AnalyticsCache cache =
                analyticsCacheRepository
                        .findByCompanyIdAndAnalysisType(company.getId(), ANALYSIS_TYPE)
                        .orElse(null);

        if (cache != null
                && cache.getAnalysisJson() != null
                && cache.getUpdatedAt() != null
                && ChronoUnit.MINUTES.between(cache.getUpdatedAt(), LocalDateTime.now()) < CACHE_TTL_MINUTES) {

            try {
                return objectMapper.readValue(cache.getAnalysisJson(), DashboardAIResponse.class);
            } catch (JsonProcessingException e) {
                // Fall through and regenerate if the cached payload is somehow unreadable.
            }
        }

        // Cache missed (or was unreadable): we're about to make a real LLM
        // call. Guard against a near-simultaneous duplicate request for the
        // same company doing the same thing before this request's cache
        // write lands.
        enforceLlmCallCooldown(company.getId());

        DashboardAIResponse response = callModel(dashboardResponse);

        try {
            String responseJson = objectMapper.writeValueAsString(response);

            AnalyticsCache toSave = cache != null ? cache : AnalyticsCache.builder()
                    .company(company)
                    .analysisType(ANALYSIS_TYPE)
                    .createdAt(LocalDateTime.now())
                    .build();

            toSave.setAnalysisJson(responseJson);
            toSave.setPeriodStart(LocalDate.now());
            toSave.setPeriodEnd(LocalDate.now());
            toSave.setUpdatedAt(LocalDateTime.now());

            analyticsCacheRepository.save(toSave);
        } catch (JsonProcessingException e) {
            // Insights already computed; a cache-write failure shouldn't fail the request.
        }

        return response;
    }

    /**
     * Rejects the call if this company made a real LLM call within the
     * cooldown window; otherwise records this attempt as the new last call.
     * Cache hits never reach this method, so legitimate repeated requests
     * within the 30-minute cache TTL are unaffected.
     */
    private void enforceLlmCallCooldown(Long companyId) {
        Instant now = Instant.now();

        Instant[] rejectedAt = new Instant[1];
        lastLlmCallByCompanyId.compute(companyId, (id, lastCall) -> {
            if (lastCall != null && Duration.between(lastCall, now).compareTo(LLM_CALL_COOLDOWN) < 0) {
                rejectedAt[0] = lastCall;
                return lastCall;
            }
            return now;
        });

        if (rejectedAt[0] != null) {
            long elapsedSeconds = Duration.between(rejectedAt[0], now).getSeconds();
            long retryAfterSeconds = Math.max(1, LLM_CALL_COOLDOWN.getSeconds() - elapsedSeconds);
            throw new RateLimitExceededException(
                    "AI dashboard insights were just requested for this company. Please wait a few seconds and try again.",
                    retryAfterSeconds);
        }
    }

    private DashboardAIResponse callModel(DashboardResponse dashboardResponse) {

        try {

            String dashboardJson =
                    objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(dashboardResponse);

            return chatClient.prompt()

                    .system("""
                            You are an expert CFO, Financial Analyst,
                            Business Consultant and Inventory Planner.

                            All monetary figures in the dashboard data,
                            and in every response you write, are in
                            Indian Rupees (INR). Always use the ₹ symbol
                            (never $ or USD) when referencing amounts.

                            Analyze the dashboard carefully.

                            Base every conclusion ONLY on the supplied data.

                            Never invent numbers.

                            If insufficient data exists,
                            return an empty list or null.

                            Return ONLY JSON matching the DashboardAIResponse class.

                            Do not return markdown.
                            Do not wrap the JSON inside ``` blocks.
                            """)

                    .user(buildPrompt(dashboardJson))

                    .call()

                    .entity(DashboardAIResponse.class);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Unable to serialize dashboard data.",
                    e
            );
        }
    }

    private String buildPrompt(String dashboardJson) {

        return """
                Dashboard Data

                %s

                Perform the following analysis.

                1. Business Summary
                   - Overall company performance
                   - Revenue trends
                   - Customer behaviour
                   - Product performance

                2. Growth Prediction
                   - Predict future growth using available trends.

                3. Recommendations
                   - Provide actionable business recommendations.

                4. Customer Insights
                   - Best customers
                   - Customers at risk
                   - Customers with unusual behaviour

                5. Product Insights
                   - Best sellers
                   - Slow movers
                   - High revenue products

                6. Inventory Suggestions
                   - Overstocked products
                   - Understocked products
                   - Suggested reorder actions

                7. Payment Risks
                   - Late payments
                   - High outstanding customers
                   - Collection risks

                8. Profitability Analysis
                   - High margin products
                   - Low margin products
                   - Overall profitability

                9. Price Recommendations
                   - Products that can increase price
                   - Products that require discounts
                   - Pricing opportunities

                10. Demand Forecast
                    - Expected demand
                    - Seasonal demand
                    - Inventory planning

                11. Cash Flow Insights
                    - Cash flow health
                    - Liquidity observations
                    - Outstanding collections

                12. Overall Business Score
                    - Score from 0 to 100
                    - Include short explanation.

                IMPORTANT

                Return ONLY valid JSON.

                The JSON MUST match DashboardAIResponse exactly.

                Do not explain anything.

                Do not use markdown.

                Do not add additional fields.
                """
                .formatted(dashboardJson);
    }

}