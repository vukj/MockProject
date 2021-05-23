package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyRankingProfileDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileDetailWrapperList {

    private List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList;

    public ProfileDetailWrapperList() {
    }

    public ProfileDetailWrapperList(List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList) {
        this.competencyRankingProfileDetailsList = competencyRankingProfileDetailsList;
    }
}
