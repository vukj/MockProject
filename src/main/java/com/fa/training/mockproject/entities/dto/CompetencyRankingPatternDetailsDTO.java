package com.fa.training.mockproject.entities.dto;


import com.fa.training.mockproject.entities.CompetencyComponentDetails;
import com.fa.training.mockproject.entities.RankingLevelRequirementPatterns;
import com.fa.training.mockproject.enumeric.EvidenceTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class CompetencyRankingPatternDetailsDTO {

    private int competencyPatternDetailId;

    private byte rankingWeight;

    @Enumerated(EnumType.STRING)
    private EvidenceTypes evidenceTypes;

    private String pointDetails;

    @JsonIgnoreProperties(value = {"competencyRankingPatternDetailsList"})
    private CompetencyComponentDetails competencyComponentDetails;

    @JsonIgnoreProperties(value = {"competencyRankingPatternDetails", "proficiencyLevels", "maxProficiencyLevel"})
    private RankingLevelRequirementPatterns rankingLevelRequirementPatterns;

}
