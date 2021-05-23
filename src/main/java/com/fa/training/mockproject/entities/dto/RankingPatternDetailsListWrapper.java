package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyRankingPatternDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RankingPatternDetailsListWrapper {

    private List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList;

    public List<CompetencyRankingPatternDetails> getCompetencyRankingPatternDetailsList() {
        return competencyRankingPatternDetailsList;
    }

    public void setCompetencyRankingPatternDetailsList(List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList) {
        this.competencyRankingPatternDetailsList = competencyRankingPatternDetailsList;
    }
}
