package com.fa.training.mockproject.entities;


import com.fa.training.mockproject.enumeric.EvidenceTypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class CompetencyRankingPatternDetails extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int competencyPatternDetailId;

    private byte rankingWeight;//

    @Enumerated(EnumType.STRING)
    private EvidenceTypes evidenceTypes;

    @ManyToOne
    @JoinColumn(name = "competencyRankingPatterns")
    @JsonIgnoreProperties(value = {"jobRoles", "periodPatternList", "competencyRankingPatternDetailsList", "competencyRankingProfilesList"})
    private CompetencyRankingPatterns competencyRankingPatterns;//

    @ManyToOne
    @JoinColumn(name = "competencyComponentDetails")
    @JsonIgnoreProperties(value = {"competencyRankingPatternDetailsList"})
    private CompetencyComponentDetails competencyComponentDetails;//

    @ManyToOne
    @JoinColumn(name = "dataSources")
    @JsonIgnore
    private DataSources dataSources;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rankingLevelRequirementPatterns")
    private RankingLevelRequirementPatterns rankingLevelRequirementPatterns;

    @OneToMany(mappedBy = "competencyRankingPatternDetails", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList;

    public CompetencyRankingPatternDetails() {
    }

    public CompetencyRankingPatternDetails(byte rankingWeight, CompetencyRankingPatterns competencyRankingPatterns,
                                           CompetencyComponentDetails competencyComponentDetails,
                                           RankingLevelRequirementPatterns rankingLevelRequirementPatterns,
                                           EvidenceTypes evidenceTypes) {
        this.rankingWeight = rankingWeight;
        this.competencyRankingPatterns = competencyRankingPatterns;
        this.competencyComponentDetails = competencyComponentDetails;
        this.rankingLevelRequirementPatterns = rankingLevelRequirementPatterns;
        this.evidenceTypes = evidenceTypes;
    }
}
