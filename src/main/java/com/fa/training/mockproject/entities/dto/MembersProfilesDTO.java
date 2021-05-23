package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.Domains;
import com.fa.training.mockproject.entities.JobRoles;
import com.fa.training.mockproject.entities.PeriodPattern;
import com.fa.training.mockproject.enumeric.StatusTypes;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MembersProfilesDTO {
    private List<CompetencyRankingProfiles> memberRankingProfileList;
    private List<Domains> domainsList;
    private List<JobRoles> jobRolesList;
    private List<PeriodPattern> periodPatternList;
    private List<StatusTypes> statusTypesList;
    private List<Integer> listPage;
    private Integer currentPage;
}
