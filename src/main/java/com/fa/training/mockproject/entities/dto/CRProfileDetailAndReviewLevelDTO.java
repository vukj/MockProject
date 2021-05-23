package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyRankingProfileDetails;
import com.fa.training.mockproject.entities.CompetencyReviewRankings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CRProfileDetailAndReviewLevelDTO {
    private CompetencyRankingProfileDetails competencyRankingProfileDetails;
    private CompetencyReviewRankings competencyReviewRankings;
}
