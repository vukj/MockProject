package com.fa.training.mockproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class ProficiencyLevelTypes extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short proficiencyLevelTypeId;

    @Column(length = 1000)
    private String proficiencyLevelTypeName;

    @Length(max = 100)
    private String proficiencyLevelTypeDescription;

    @OneToMany(mappedBy = "proficiencyLevelTypes", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"competencyRankingProfileDetailsList", "proficiencyLevelTypes", "competencyReviewRankingsList", "rankingLevelRequirementPatternsList"})
    private List<ProficiencyLevels> proficiencyLevelsList;//

    @OneToMany(mappedBy = "proficiencyLevelTypes")
    private List<CompetencyComponentDetails> competencyComponentDetailsList;

    public ProficiencyLevelTypes() {
    }

    public ProficiencyLevelTypes(@Length(max = 30) String proficiencyLevelTypeName) {
        this.proficiencyLevelTypeName = proficiencyLevelTypeName;
    }

    public ProficiencyLevelTypes(String proficiencyLevelTypeName, String proficiencyLevelTypeDescription) {
        this.proficiencyLevelTypeName = proficiencyLevelTypeName;
        this.proficiencyLevelTypeDescription = proficiencyLevelTypeDescription;
    }
}
