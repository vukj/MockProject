package com.fa.training.mockproject.entities;

import com.fa.training.mockproject.enumeric.StatusTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class CompetencyRankingProfiles extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rankingProfileId;//

    @NotNull
    @Length(max = 50)
    private String profileTitle;

    @Enumerated(EnumType.STRING)
    private StatusTypes statusTypes;//

    private String selfRakingPoints;//

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date submittedDate;

    @ManyToOne
    @JoinColumn(name = "employees")
    @JsonIgnoreProperties(value = {"userAccounts", "competencyRankingProfilesList", "competencyResultsOfLeaderList", "competencyReviewRankingsList"})
    private Employees employees;//

    @ManyToOne
    @JoinColumn(name = "competencyRankingPatterns")
    @JsonIgnoreProperties(value = {"competencyRankingPatternDetailsList", "competencyRankingProfilesList"})
    private CompetencyRankingPatterns competencyRankingPatterns;//

    @OneToMany(mappedBy = "competencyRankingProfiles", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties(value = {"competencyRankingProfiles"})
    private List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList;

    @OneToOne(mappedBy = "competencyRankingProfile", cascade = CascadeType.REMOVE)
    @JoinColumn(name = "competencyResults")
    @JsonIgnoreProperties(value = {"approveRanking", "approvedBy"})
    private CompetencyResults competencyResults;
}
