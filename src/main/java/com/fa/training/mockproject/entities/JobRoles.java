package com.fa.training.mockproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class JobRoles extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobRoleId;

    @Length(max = 50)
    @NotNull
    private String jobRoleName;//

    @ManyToOne
    @JoinColumn(name = "domains")
    @JsonIgnoreProperties(value = {"domainTypes", "jobRolesList", "employeesList"})
    private Domains domains;//

    @OneToMany(mappedBy = "jobRoles")
    private List<CompetencyRankingPatterns> competencyRankingPatternsList;

    @OneToMany(mappedBy = "jobRoles")
    private List<Employees> employeesList;

    @OneToMany(mappedBy = "approveRanking")
    private List<CompetencyResults> competencyResultsApproveList;

    public JobRoles() {
    }

    public JobRoles(@Length(max = 50) String jobRoleName, Domains domains) {
        this.jobRoleName = jobRoleName;
        this.domains = domains;
    }
}
