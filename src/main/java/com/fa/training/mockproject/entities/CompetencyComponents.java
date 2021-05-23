package com.fa.training.mockproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class CompetencyComponents extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int componentId;//

    private byte componentWeight;

    @Column(length = 50)
    @NonNull
    private String componentName;//

    @JsonIgnore
    private String componentDescription;

    @OneToMany(mappedBy = "competencyComponents")
    @JsonIgnore
    private List<CompetencyComponentDetails> competencyComponentDetailsList;

    @ManyToMany
    @JsonIgnore
    private List<CompetencyRankingPatterns> competencyRankingPatternsList;

    public CompetencyComponents() {
    }

    public CompetencyComponents(String componentName) {
        this.componentName = componentName;
    }
}
