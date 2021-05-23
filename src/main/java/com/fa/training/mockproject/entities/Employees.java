package com.fa.training.mockproject.entities;

import com.fa.training.mockproject.enumeric.GenderTypes;
import com.fa.training.mockproject.enumeric.MaritalStatus;
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
public class Employees extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    @Length(max = 30)
    private String fullName;//

    @Length(max = 10)
    private String phone;

    @NotNull
    private String account;

    @Length(max = 50)
    private String address1;

    @Length(max = 50)
    private String address2;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private GenderTypes genderTypes;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_accounts")
    @JsonIgnoreProperties(value = {"employees"})
    private UserAccounts userAccounts;//

    @OneToMany(mappedBy = "employees", cascade = CascadeType.PERSIST)
    private List<CompetencyRankingProfiles> competencyRankingProfilesList;

    @ManyToOne
    @JoinColumn(name = "domains")
    @JsonIgnoreProperties(value = {"domainTypes", "jobRolesList", "employeesList"})
    private Domains domains;//

    @ManyToOne
    @JoinColumn(name = "job_roles")
    @JsonIgnoreProperties(value = {"competencyRankingPatternsList", "employeesList", "competencyResultsApproveList", "domains"})
    private JobRoles jobRoles;//

    @OneToMany(mappedBy = "reviewer")
    private List<CompetencyResults> competencyResultsOfReviewerList;

    @OneToMany(mappedBy = "approvedBy")
    private List<CompetencyResults> competencyResultsOfApproveByList;

    @OneToMany(mappedBy = "reviewer")
    private List<CompetencyReviewRankings> competencyReviewRankingsList;

    public Employees() {
    }

    public Employees(String fullName) {
        this.fullName = fullName;
    }
}
