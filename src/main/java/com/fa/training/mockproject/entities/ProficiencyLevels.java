package com.fa.training.mockproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class ProficiencyLevels extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int proficiencyLevelId;

    private byte proficiencyLevel; //

    private String proficiencyLevelName; //

    private String proficiencyLevelDescription;

    @OneToMany(mappedBy = "proficiencyLevels")
    @JsonIgnoreProperties(value = {"proficiencyLevels", "competencyRankingProfileDetailsList"})
    private List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList;

    @ManyToOne
    @JoinColumn(name = "proficiencyLevelTypes")
    private ProficiencyLevelTypes proficiencyLevelTypes;

    @OneToMany(mappedBy = "proficiencyLevels")
    private List<CompetencyReviewRankings> competencyReviewRankingsList;

    @OneToMany(mappedBy = "maxProficiencyLevel")
    private List<RankingLevelRequirementPatterns> rankingLevelRequirementPatternsList;

    public ProficiencyLevels() {
    }

    public ProficiencyLevels(byte proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public ProficiencyLevels(byte proficiencyLevel, String proficiencyLevelName, ProficiencyLevelTypes proficiencyLevelTypes) {
        this.proficiencyLevel = proficiencyLevel;
        this.proficiencyLevelName = proficiencyLevelName;
        this.proficiencyLevelTypes = proficiencyLevelTypes;
    }
}
