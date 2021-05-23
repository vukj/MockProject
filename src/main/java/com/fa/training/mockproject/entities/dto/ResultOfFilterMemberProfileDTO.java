package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultOfFilterMemberProfileDTO {
    @JsonIgnoreProperties(value = {"competencyRankingProfileDetailsList"})
    private List<CompetencyRankingProfiles> competencyRankingProfilesFilterList;
    private List<Integer> listPage;
    private int currentPage;
}
