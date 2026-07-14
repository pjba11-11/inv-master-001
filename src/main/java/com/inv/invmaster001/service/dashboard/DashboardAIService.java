package com.inv.invmaster001.service.dashboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inv.invmaster001.dto.response.dashboard.DashboardAIResponse;
import com.inv.invmaster001.dto.response.dashboard.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardAIService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public DashboardAIResponse generateInsights(
            DashboardResponse dashboardResponse) {

        try {

            String dashboardJson =
                    objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(dashboardResponse);

            return chatClient.prompt()

                    .system("""
                            You are an expert CFO, Financial Analyst,
                            Business Consultant and Inventory Planner.

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