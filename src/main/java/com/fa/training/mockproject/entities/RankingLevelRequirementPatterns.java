package com.fa.training.mockproject.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class RankingLevelRequirementPatterns extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rankingLevelRequirementPatternId;

    @ManyToOne
    private ProficiencyLevels proficiencyLevels;

    @OneToOne(mappedBy = "rankingLevelRequirementPatterns")
    @JoinColumn(name = "competencyRankingPatternDetails")
    private CompetencyRankingPatternDetails competencyRankingPatternDetails;

    @ManyToOne
    private ProficiencyLevels maxProficiencyLevel;

    public RankingLevelRequirementPatterns() {
    }

    public RankingLevelRequirementPatterns(ProficiencyLevels proficiencyLevelRequirement, ProficiencyLevels maxProficiencyLevel) {
        this.proficiencyLevels = proficiencyLevelRequirement;
        this.maxProficiencyLevel = maxProficiencyLevel;
    }
}
