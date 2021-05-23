package com.fa.training.mockproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class CompetencyReviewRankings extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int competencyReviewRankingsId;//

    @OneToOne(mappedBy = "competencyReviewRankings")
    @JsonIgnore
    private CompetencyRankingProfileDetails competencyRankingProfileDetails;

    @ManyToOne
    @JoinColumn(name = "reviewer", columnDefinition = "int default 0")
    @JsonIgnore
    private Employees reviewer;

    @ManyToOne
    @JoinColumn(name = "proficiencyLevels", columnDefinition = "int default 0")
    @JsonIgnoreProperties(value = {"competencyRankingProfileDetailsList", "proficiencyLevelTypes", "competencyReviewRankingsList", "rankingLevelRequirementPatternsList"})
    private ProficiencyLevels proficiencyLevels;

}
