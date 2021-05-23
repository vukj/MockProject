package com.fa.training.mockproject.entities.dto;


import com.fa.training.mockproject.entities.CompetencyRankingPatternDetails;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetencyRankingProfileDetailsDTO {

    private int rankingProfileDetailId;

    private String pointDetails;

    private String reviewPointDetails;

    private String requirePointDetails;

    private String name;

    private CompetencyRankingPatternDetails competencyRankingPatternDetails;
}
