package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyComponents;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatternSummaryDTO {
    @JsonIgnoreProperties(value = {"competencyComponentDetailsList", "competencyRankingPatternsList"})
    private CompetencyComponents competencyComponents;
    private String totalWeight;
    private String evidence;
    private String basePoint;
    private String status;
}
