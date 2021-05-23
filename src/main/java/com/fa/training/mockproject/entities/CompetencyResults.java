package com.fa.training.mockproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class CompetencyResults extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int competencyResultsId;//

    private float reviewRakingPoints;//

    private String cmtAction;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date reviewedDate;//

    @ManyToOne
    @JoinColumn(name = "reviewer")
    @JsonIgnoreProperties(value = {"userAccounts", "competencyRankingProfilesList", "domains", "jobRoles", "competencyResultsOfReviewerList",
            "competencyReviewRankingsList", "competencyResultsOfApproveByList"})
    private Employees reviewer;//

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "competencyRankingProfile")
    @JsonIgnoreProperties(value = {"competencyRankingProfileDetailsList", "competencyResults"})
    private CompetencyRankingProfiles competencyRankingProfile;//

    @ManyToOne
    @JoinColumn(name = "approveRanking")
    @JsonIgnore
    private JobRoles approveRanking;

    @ManyToOne
    @JoinColumn(name = "approvedBy")
    @JsonIgnore
    private Employees approvedBy;

}
