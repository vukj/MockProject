package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PatternDTO {

    private int competencyPatternId;

    @JsonIgnoreProperties(value = {"competencyRankingPatternsList", "employeesList", "competencyResultsApproveList"})
    private JobRoles jobRoles;

    @JsonIgnoreProperties(value = {"jobRolesList", "employeesList", "domainTypes"})
    private Domains domains;

    // Tan code
    @JsonIgnoreProperties(value = {"competencyRankingPatternsList"})
    private PeriodPattern periodPattern;

    private Date created;

    private ActiveStatus activeStatus;

    @JsonIgnoreProperties(value = {"competencyComponentDetailsList", "competencyRankingPatternsList"})
    private List<CompetencyComponents> competencyComponentsList;

    @JsonIgnoreProperties(value = {"competencyRankingProfileDetailsList", "competencyResultsList"})
    private List<CompetencyRankingProfiles> competencyRankingProfilesList;

    private boolean statusPermitCopy;
}
