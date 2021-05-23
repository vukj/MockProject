package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.enumeric.StatusTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CompetencyRankingProfilesRepository extends JpaRepository<CompetencyRankingProfiles, Integer> {
    boolean existsByProfileTitleAndCompetencyRankingPatterns_PeriodPattern_PeriodIdAndCompetencyRankingPatterns_JobRoles_JobRoleIdAndCompetencyRankingPatterns_JobRoles_Domains_DomainId(String title, long periodId, int jobrolesId, int domainId);

    CompetencyRankingProfiles findByRankingProfileId(int id);

    CompetencyRankingProfiles findFirstByStatusTypesOrderByModifiedDesc(StatusTypes statusTypes);

    CompetencyRankingProfiles findFirstByEmployees_EmployeeIdOrderByModifiedDesc(int employeeId);

    Page<CompetencyRankingProfiles> findByStatusTypesOrStatusTypesOrStatusTypesOrStatusTypesOrderByCompetencyResults_ReviewedDateDesc
            (StatusTypes statusTypes, StatusTypes statusTypes1, StatusTypes statusTypes2, StatusTypes statusTypes3, Pageable pageable);

    Page<CompetencyRankingProfiles> findByCompetencyRankingPatterns_JobRoles_Domains_DomainNameAndStatusTypesOrStatusTypesOrStatusTypesOrStatusTypesOrderBySubmittedDateDesc
            (String domain, StatusTypes statusTypes, StatusTypes statusTypes1, StatusTypes statusTypes2, StatusTypes statusTypes3, Pageable pageable);

    Page<CompetencyRankingProfiles> findByEmployees_EmployeeIdAndStatusTypesOrStatusTypesOrStatusTypesOrStatusTypesOrderByCreatedDesc
            (int idEmployee, StatusTypes statusTypes, StatusTypes statusTypes1, StatusTypes statusTypes2, StatusTypes statusTypes3, Pageable pageable);

    Page<CompetencyRankingProfiles> findAllByEmployeesOrderByCreatedDesc(Employees employees, Pageable pageable);

    @Query(value = "SELECT p FROM CompetencyRankingProfiles p WHERE (?1 IS NULL OR p.profileTitle LIKE %?1%) " +
            "AND (?2 IS NULL OR p.competencyRankingPatterns.periodPattern.periodId = ?2) " +
            "AND (?3 IS NULL OR cast(p.created AS date) = ?3) " +
            "AND (?4 IS NULL OR p.statusTypes = ?4) " +
            "AND (?5 IS NULL OR p.competencyRankingPatterns.jobRoles.jobRoleId = ?5) " +
            "AND p.employees = ?6 ORDER BY p.created DESC ")
    Page<CompetencyRankingProfiles> findAllByFilter(String title, Long periodId, Date created, String status, Integer rolesId, Employees employees, Pageable pageable);

    @Query(value = "SELECT p FROM CompetencyRankingProfiles p WHERE (:rolesId IS NULL OR p.competencyRankingPatterns.jobRoles.jobRoleId = :rolesId) " +
            "AND (:periodId IS NULL OR p.competencyRankingPatterns.periodPattern.periodId = :periodId) " +
            "AND (:memberName IS NULL OR p.employees.fullName LIKE %:memberName%) " +
            "AND (:submitted1 IS NULL OR p.submittedDate BETWEEN :submitted1 and :submitted2) " +
            "AND (:domain IS NULL OR p.competencyRankingPatterns.jobRoles.domains.domainName = :domain) " +
            "AND ((:statusTypes1 IS NULL OR p.statusTypes = :statusTypes1) " +
            "OR (:statusTypes2 IS NULL OR p.statusTypes = :statusTypes2) " +
            "OR (:statusTypes3 IS NULL OR p.statusTypes = :statusTypes3) " +
            "OR (:statusTypes4 IS NULL OR p.statusTypes = :statusTypes4)) " +
            "AND (:statusTypesFilter IS NULL OR p.statusTypes = :statusTypesFilter) " +
            "ORDER BY p.submittedDate desc")
    Page<CompetencyRankingProfiles> leaderFindMemberProfileByFilter(@Param("rolesId") Integer rolesId, @Param("periodId") Long periodId, @Param("memberName") String memberName,
                                                                    @Param("submitted1") Date submitted1, @Param("submitted2") Date submitted2,
                                                                    @Param("domain") String domain,
                                                                    @Param("statusTypes1") StatusTypes statusTypes1, @Param("statusTypes2") StatusTypes statusTypes2,
                                                                    @Param("statusTypes3") StatusTypes statusTypes3, @Param("statusTypes4") StatusTypes statusTypes4,
                                                                    @Param("statusTypesFilter") StatusTypes statusTypesFilter, Pageable pageable);

    @Query(value = "SELECT p FROM CompetencyRankingProfiles p WHERE (:rolesId IS NULL OR p.competencyRankingPatterns.jobRoles.jobRoleId = :rolesId) " +
            "AND (:periodId IS NULL OR p.competencyRankingPatterns.periodPattern.periodId = :periodId) " +
            "AND (:memberName IS NULL OR p.employees.fullName LIKE %:memberName%) " +
            "AND (:reviewed1 IS NULL OR p.competencyResults.reviewedDate BETWEEN :reviewed1 and :reviewed2) " +
            "AND (:domain IS NULL OR p.competencyRankingPatterns.jobRoles.domains.domainName = :domain) " +
            "AND ((:statusTypes1 IS NULL OR p.statusTypes = :statusTypes1) " +
            "OR (:statusTypes2 IS NULL OR p.statusTypes = :statusTypes2) " +
            "OR (:statusTypes3 IS NULL OR p.statusTypes = :statusTypes3) " +
            "OR (:statusTypes4 IS NULL OR p.statusTypes = :statusTypes4)) " +
            "AND (:statusTypesFilter IS NULL OR p.statusTypes = :statusTypesFilter) " +
            "ORDER BY p.competencyResults.reviewedDate desc")
    Page<CompetencyRankingProfiles> managerFindMemberProfileByFilter(@Param("rolesId") Integer rolesId, @Param("periodId") Long periodId, @Param("memberName") String memberName,
                                                                     @Param("reviewed1") Date reviewed1, @Param("reviewed2") Date reviewed2,
                                                                     @Param("domain") String domain,
                                                                     @Param("statusTypes1") StatusTypes statusTypes1, @Param("statusTypes2") StatusTypes statusTypes2,
                                                                     @Param("statusTypes3") StatusTypes statusTypes3, @Param("statusTypes4") StatusTypes statusTypes4,
                                                                     @Param("statusTypesFilter") StatusTypes statusTypesFilter, Pageable pageable);
}
