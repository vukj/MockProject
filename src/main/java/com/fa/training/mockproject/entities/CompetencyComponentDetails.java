package com.fa.training.mockproject.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class CompetencyComponentDetails extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int componentDetailId;

    @Length(max = 50)
    @NonNull
    private String componentDetailName;//

    private String componentDetailDescription;

    @ManyToOne
    @JoinColumn(name = "competencyComponents")
    @JsonIgnoreProperties(value = {"competencyComponentDetailsList", "competencyRankingPatternsList"})
    private CompetencyComponents competencyComponents;//

    @OneToMany(mappedBy = "competencyComponentDetails")
    @JsonIgnoreProperties(value = {"competencyComponentDetailsList"})
    private List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList;

    @ManyToOne
    @JoinColumn(name = "proficiencyLevelTypes")
    @JsonIgnoreProperties(value = {"competencyComponentDetailsList"})
    private ProficiencyLevelTypes proficiencyLevelTypes;//

    @ManyToMany
    private List<DataSources> dataSourcesList;

    public CompetencyComponentDetails() {
    }

    public CompetencyComponentDetails(@Length(max = 50) String componentDetailName,
                                      ProficiencyLevelTypes proficiencyLevelTypes,
                                      CompetencyComponents competencyComponents,
                                      List<DataSources> dataSourcesList) {
        this.componentDetailName = componentDetailName;
        this.proficiencyLevelTypes = proficiencyLevelTypes;
        this.competencyComponents = competencyComponents;
        this.dataSourcesList = dataSourcesList;

    }
}
