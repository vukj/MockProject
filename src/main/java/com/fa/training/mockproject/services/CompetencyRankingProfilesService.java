package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.CompetencyRankingProfileDetails;
import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.CompetencyResults;
import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.entities.dto.ProfileDTO;
import com.fa.training.mockproject.entities.dto.ProfileSummaryDTO;
import com.fa.training.mockproject.enumeric.StatusTypes;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface CompetencyRankingProfilesService {
    boolean existsByProfileTitleAndCompetencyRankingPatterns_PeriodPattern_PeriodIdAndCompetencyRankingPatterns_JobRoles_JobRoleIdAndCompetencyRankingPatterns_JobRoles_Domains_DomainId(String title, long periodId, int jobrolesId, int domainId);

    CompetencyRankingProfiles findById(int id);

    void save(ProfileDTO profileDTO);

    void save(CompetencyRankingProfiles competencyRankingProfiles);

    void addCompetencyRankingProfiles(CompetencyRankingProfiles competencyRankingProfiles, String email);

    ProfileDTO findFirstByEmployees_EmployeeIdOrderByModifiedDesc(int employeeId);

    ProfileDTO findFirstByStatusTypesOrderByModifiedDesc(StatusTypes statusTypes);

    CompetencyRankingProfiles checkNullAndGetCompetencyRankingProfiles(CompetencyRankingProfiles competencyRankingProfiles, int rankingProfileId);

    List<CompetencyRankingProfiles> findAllProfile();

    void deleteById(int id);

    String calculateSelfCRProfilePoint(CompetencyRankingProfiles competencyRankingProfiles);

    CompetencyResults reviewCRProfileDetail(Employees reviewer, CompetencyRankingProfiles cRProfileReview, int competencyComponentId,
                                            List<Integer> proficiencyLevelList);

    List<ProfileSummaryDTO> calculateTotalWeightForProfileSummary(Employees reviewer, List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList);

    Page<ProfileDTO> findAllByNoFilter(int empId, int page);

    Page<ProfileDTO> findAllByFilter(String title, Long periodId, Date created, String status, Integer rolesId, int empId, int page);
}
