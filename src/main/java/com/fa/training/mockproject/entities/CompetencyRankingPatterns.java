package com.fa.training.mockproject.entities;

import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class CompetencyRankingPatterns extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int competencyPatternId;

    private float componentWeight;

    @ManyToOne
    @JoinColumn(name = "jobRoles")
    @JsonIgnoreProperties(value = {"competencyRankingPatternsList", "employeesList", "competencyResultsApproveList"})
    private JobRoles jobRoles;//

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = {"competencyRankingPatternsList"})
    private PeriodPattern periodPattern;//

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"competencyComponentDetailsList", "competencyRankingPatternsList"})
    private List<CompetencyComponents> competencyComponentsList;//

    @OneToMany(mappedBy = "competencyRankingPatterns", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList;

    @OneToMany(mappedBy = "competencyRankingPatterns", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<CompetencyRankingProfiles> competencyRankingProfilesList;

    public CompetencyRankingPatterns() {
    }
}
