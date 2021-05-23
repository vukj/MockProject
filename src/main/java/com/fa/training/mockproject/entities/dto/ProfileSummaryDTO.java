package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyComponents;
import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileSummaryDTO {
    private CompetencyRankingPatterns competencyRankingPatterns;
    private CompetencyComponents patternCompetencyComponents;

    @JsonIgnoreProperties(value = {"competencyComponentDetailsList", "competencyRankingPatternsList"})
    private CompetencyComponents competencyComponents;
    private String totalWeight;
    private String selfRankWeight;
    private String reviewRankWeight;
    //    Code Vu
    private String patternRankWeight;
    private CompetencyRankingProfiles competencyRankingProfiles;
    private int status;
}
