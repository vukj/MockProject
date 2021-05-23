package com.fa.training.mockproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class CompetencyRankingProfileDetails extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rankingProfileDetailId;//

    private String selfRankingNotes;

    @ManyToOne
    @JoinColumn(name = "competencyRankingProfiles")
    @JsonIgnoreProperties(value = {"employees", "competencyRankingPatterns", "competencyResults"}) //Code Vu
    private CompetencyRankingProfiles competencyRankingProfiles;

    @ManyToOne
    @JoinColumn(name = "proficiencyLevels")
    @JsonIgnoreProperties(value = {"proficiencyLevelTypes", "competencyRankingProfileDetailsList",
            "competencyReviewRankingsList", "rankingLevelRequirementPatternsList"})
    private ProficiencyLevels proficiencyLevels;//

    @ManyToOne
    @JoinColumn(name = "competencyRankingPatternDetails")
    @JsonIgnoreProperties(value = {"rankingLevelRequirementPatterns", "competencyRankingProfileDetailsList"})
    private CompetencyRankingPatternDetails competencyRankingPatternDetails; //

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private CompetencyReviewRankings competencyReviewRankings;

    @ManyToOne
    @JoinColumn(name = "dataSources")
    @JsonIgnoreProperties(value = {"competencyRankingProfileDetailsList", "competencyComponentDetailsList"})
    private DataSources dataSources;
}
