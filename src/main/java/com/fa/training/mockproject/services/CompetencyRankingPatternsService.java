package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.entities.dto.PatternDTO;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompetencyRankingPatternsService {

    CompetencyRankingPatterns findById(int id);

    List<CompetencyRankingPatterns> findAll();

    void save(CompetencyRankingPatterns competencyRankingPatterns);

    void update(CompetencyRankingPatterns competencyRankingPatterns);

    void delete(CompetencyRankingPatterns competencyRankingPatterns);

    List<CompetencyRankingPatterns> getAllByPeriodPatternPeriodId(Long id);

    List<CompetencyRankingPatterns> findAllByJobRoles_Domains_DomainIdAndPeriodPattern_PeriodIdAndActiveStatus(int domainId, Long periodId, ActiveStatus activeStatus);

    List<CompetencyRankingPatterns> findAllByJobRoles_Domains_DomainIdAndActiveStatus(int domainId, ActiveStatus activeStatus);

    List<CompetencyRankingPatterns> findCompetencyRankingPatternsByPeriodPattern_PeriodIdAndJobRoles_Domains_DomainId(Long periodId, int domainId);

    CompetencyRankingPatterns findByPeriodIdAndDomainIdAndJobRolesId(Long periodId, int domainId, int jobRoleId);

    Page<PatternDTO> findAllCompetencyRankingPatternsNoFilter(int page);

    Page<PatternDTO> findAllCompetencyRankingPatternsByFilter(String title, String domainName, String jobRolesName, Long periodId, String status, int page);

    List<CompetencyRankingPatterns> getAllByJobRoleAndDomain(CompetencyRankingPatterns competencyRankingPatterns);
}
