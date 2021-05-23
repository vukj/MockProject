package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.enumeric.StatusTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProfileDTO extends CommonGenericClass {

    private int rankingProfileId;

    private String profileTitle;

    @Enumerated(EnumType.STRING)
    private StatusTypes statusTypes;

    private String selfRakingPoints;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date submittedDate;

    @JsonIgnoreProperties(value = {"userAccounts", "competencyRankingProfilesList", "competencyResultsOfLeaderList", "competencyReviewRankingsList"})
    private Employees employees;

    private CompetencyRankingPatterns competencyRankingPatterns;

    @JsonIgnoreProperties(value = {"competencyComponentDetails", "proficiencyLevelTypes"})
    private List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList;

    private CompetencyResults competencyResults;

    private JobRoles jobRoles;

    public ProfileDTO() {
    }

}