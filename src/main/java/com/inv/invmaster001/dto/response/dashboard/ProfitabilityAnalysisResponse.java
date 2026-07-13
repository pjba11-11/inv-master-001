package com.inv.invmaster001.dto.response.dashboard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfitabilityAnalysisResponse {

    private String summary;

    private List<String> strengths;

    private List<String> weaknesses;

}
