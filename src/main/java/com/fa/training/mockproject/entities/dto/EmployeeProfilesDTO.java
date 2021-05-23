package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.Employees;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeProfilesDTO {
    @JsonIgnoreProperties(value = {"competencyRankingProfileDetailsList", "competencyResultsList"})
    private List<CompetencyRankingProfiles> competencyRankingProfilesList;
    @JsonIgnoreProperties(value = {"competencyRankingProfilesList", "domains", "jobRoles",
            "competencyResultsOfReviewerList", "competencyResultsOfApproveByList", "competencyReviewRankingsList"})
    private Employees employees;
    private List<Integer> listPage;
    private int currentPage;
}
