package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetencyRankingPatternsRepository extends JpaRepository<CompetencyRankingPatterns, Integer> {
    CompetencyRankingPatterns findByCompetencyPatternId(int id);

    @Query("FROM CompetencyRankingPatterns p ORDER BY p.created DESC, p.periodPattern.name ASC")
    Page<CompetencyRankingPatterns> findAllByOderByCreatedDesc(Pageable pageable);

    List<CompetencyRankingPatterns> getAllByPeriodPatternPeriodId(Long id);

    List<CompetencyRankingPatterns> findCompetencyRankingPatternsByPeriodPattern_PeriodIdAndJobRoles_Domains_DomainId(Long periodId, int domainId);

    CompetencyRankingPatterns findCompetencyRankingPatternsByPeriodPattern_PeriodIdAndJobRoles_Domains_DomainIdAndJobRoles_JobRoleId(Long periodId, int domainId, int jobRolesId);

    List<CompetencyRankingPatterns> findAllByJobRoles_Domains_DomainIdAndPeriodPattern_PeriodIdAndActiveStatusOrderByPeriodPattern_YearAscPeriodPattern_nameAsc(int domainId, Long periodId, ActiveStatus activeStatus);

    List<CompetencyRankingPatterns> findAllByJobRoles_Domains_DomainIdAndActiveStatus(int domainId, ActiveStatus activeStatus);

    @Query("SELECT p FROM CompetencyRankingPatterns p WHERE (?1 IS NULL OR p.jobRoles.domains.domainName LIKE ?1% OR p.jobRoles.jobRoleName LIKE ?1% OR p.periodPattern.name LIKE ?1% OR p.periodPattern.year LIKE ?1% OR p.activeStatus LIKE ?1%) " +
            "AND (?2 IS NULL OR p.jobRoles.domains.domainName LIKE %?2% ) " +
            "AND (?3 IS NULL OR p.jobRoles.jobRoleName LIKE %?3%) " +
            "AND (?4 IS NULL OR p.periodPattern.periodId = ?4)" +
            "AND (?5 IS NULL OR p.activeStatus LIKE %?5%)" +
            "ORDER BY p.created DESC, p.periodPattern.name ASC")
    Page<CompetencyRankingPatterns> findAllCompetencyRankingPatternsByFilter(String title, String domainName, String jobRolesName, Long periodId, String status, Pageable pageable);

    @Query("SELECT p FROM CompetencyRankingPatterns p WHERE p.jobRoles.jobRoleName = ?1 AND p.jobRoles.domains.domainName = ?2")
    List<CompetencyRankingPatterns> getAllByJobRoleAndDomain(String jobRolesName, String domainName);
}
