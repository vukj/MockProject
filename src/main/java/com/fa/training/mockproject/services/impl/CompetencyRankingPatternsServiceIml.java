package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.CompetencyRankingPatternDetails;
import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.entities.dto.PatternDTO;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.repositories.CompetencyRankingPatternsRepository;
import com.fa.training.mockproject.repositories.PeriodPatternRepository;
import com.fa.training.mockproject.services.CompetencyRankingPatternsService;
import com.fa.training.mockproject.services.mapper.PatternMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CompetencyRankingPatternsServiceIml implements CompetencyRankingPatternsService {

    @Autowired
    private PeriodPatternRepository periodPatternRepository;

    @Autowired
    private CompetencyRankingPatternsRepository competencyRankingPatternsRepository;

    @Autowired
    private JobRolesServiceImpl jobRolesService;

    /**
     * Find a Competency Ranking Patterns by ID
     *
     * @param id
     * @return
     */
    @Override
    public CompetencyRankingPatterns findById(int id) {
        return competencyRankingPatternsRepository.findByCompetencyPatternId(id);
    }

    /**
     * Get list Competency Ranking Patterns
     *
     * @return
     */
    @Override
    public List<CompetencyRankingPatterns> findAll() {
        return competencyRankingPatternsRepository.findAll();
    }

    /**
     * Save a Competency Ranking Patterns
     *
     * @param competencyRankingPatterns
     */
    @Override
    public void save(CompetencyRankingPatterns competencyRankingPatterns) {
        competencyRankingPatternsRepository.save(competencyRankingPatterns);
    }

    /**
     * Update a Competency Ranking Patterns
     *
     * @param competencyRankingPatterns
     */
    @Override
    public void update(CompetencyRankingPatterns competencyRankingPatterns) {
        competencyRankingPatternsRepository.save(competencyRankingPatterns);
    }

    /**
     * Delete a Competency Ranking Patterns
     *
     * @param competencyRankingPatterns
     */
    @Override
    public void delete(CompetencyRankingPatterns competencyRankingPatterns) {
        competencyRankingPatternsRepository.delete(competencyRankingPatterns);
    }

    /**
     * Get list of patterns by period's Id
     *
     * @param id
     * @return list of patterns
     */
    @Override
    public List<CompetencyRankingPatterns> getAllByPeriodPatternPeriodId(Long id) {
        return competencyRankingPatternsRepository.getAllByPeriodPatternPeriodId(id);
    }

    /**
     * Get list of patterns base on domain's Id , period's Id and status of pattern
     *
     * @param domainId
     * @param periodId
     * @param activeStatus
     * @return list of patterns
     */
    @Override
    public List<CompetencyRankingPatterns> findAllByJobRoles_Domains_DomainIdAndPeriodPattern_PeriodIdAndActiveStatus(int domainId, Long periodId, ActiveStatus activeStatus) {
        return competencyRankingPatternsRepository.findAllByJobRoles_Domains_DomainIdAndPeriodPattern_PeriodIdAndActiveStatusOrderByPeriodPattern_YearAscPeriodPattern_nameAsc(domainId, periodId, activeStatus);
    }

    /**
     * Get list of patterns base on domain's Id and status of pattern
     *
     * @param domainId
     * @param activeStatus
     * @return list of patterns
     */
    @Override
    public List<CompetencyRankingPatterns> findAllByJobRoles_Domains_DomainIdAndActiveStatus(int domainId, ActiveStatus activeStatus) {
        return competencyRankingPatternsRepository.findAllByJobRoles_Domains_DomainIdAndActiveStatus(domainId, activeStatus);
    }

    /**
     * Get list of patterns base on period's Id and domain's Id
     *
     * @param periodId
     * @param domainId
     * @return list of patterns
     */
    @Override
    public List<CompetencyRankingPatterns> findCompetencyRankingPatternsByPeriodPattern_PeriodIdAndJobRoles_Domains_DomainId(Long periodId, int domainId) {
        return competencyRankingPatternsRepository.findCompetencyRankingPatternsByPeriodPattern_PeriodIdAndJobRoles_Domains_DomainId(periodId, domainId);
    }

    /**
     * Get pattern by period's Id, domain's Id, jobRole's Id
     *
     * @param periodId
     * @param domainId
     * @param jobRoleId
     * @return pattern
     */
    @Override
    public CompetencyRankingPatterns findByPeriodIdAndDomainIdAndJobRolesId(Long periodId, int domainId, int jobRoleId) {
        return competencyRankingPatternsRepository.findCompetencyRankingPatternsByPeriodPattern_PeriodIdAndJobRoles_Domains_DomainIdAndJobRoles_JobRoleId(periodId, domainId, jobRoleId);
    }

    /**
     * Support paging with no search
     *
     * @param page
     * @return Page<PatternDTO>
     */
    @Override
    public Page<PatternDTO> findAllCompetencyRankingPatternsNoFilter(int page) {
        Page<CompetencyRankingPatterns> competencyRankingPatterns = competencyRankingPatternsRepository.findAllByOderByCreatedDesc(PageRequest.of(page, 5));
        Page<PatternDTO> patternDTOPage = competencyRankingPatterns.map(this::convertDTO);
        for (PatternDTO e : patternDTOPage) {
            e.setStatusPermitCopy(this.checkStatusToCopy(competencyRankingPatternsRepository.findByCompetencyPatternId(e.getCompetencyPatternId())));
        }
        return patternDTOPage;
    }

    /**
     * Support paging base on search conditions
     *
     * @param title
     * @param domainName
     * @param jobRolesName
     * @param periodId
     * @param status
     * @param page
     * @return Page<PatternDTO>
     */
    @Override
    public Page<PatternDTO> findAllCompetencyRankingPatternsByFilter(String title, String domainName, String jobRolesName, Long periodId, String status, int page) {
        Page<CompetencyRankingPatterns> competencyRankingPatterns;
        if (title == null && domainName == null && jobRolesName == null && periodId == null && status == null) {
            competencyRankingPatterns = competencyRankingPatternsRepository.findAllByOderByCreatedDesc(PageRequest.of(page, 5));
        } else {
            competencyRankingPatterns = competencyRankingPatternsRepository.findAllCompetencyRankingPatternsByFilter(title, domainName, jobRolesName, periodId, status, PageRequest.of(page, 5));
        }
        Page<PatternDTO> patternDTOPage = competencyRankingPatterns.map(this::convertDTO);
        for (PatternDTO e : patternDTOPage) {
            e.setStatusPermitCopy(this.checkStatusToCopy(competencyRankingPatternsRepository.findByCompetencyPatternId(e.getCompetencyPatternId())));
        }
        return patternDTOPage;
    }

    /**
     * Get list pattern base on competencyRankingPatterns
     *
     * @param competencyRankingPatterns
     * @return list of pattern
     */
    @Override
    public List<CompetencyRankingPatterns> getAllByJobRoleAndDomain(CompetencyRankingPatterns competencyRankingPatterns) {
        return competencyRankingPatternsRepository.getAllByJobRoleAndDomain(competencyRankingPatterns.getJobRoles().getJobRoleName(), competencyRankingPatterns.getJobRoles().getDomains().getDomainName());
    }

    /**
     * Support convert from pattern to patternDTO
     *
     * @param competencyRankingPatterns
     * @return PatternDTO
     */
    public PatternDTO convertDTO(CompetencyRankingPatterns competencyRankingPatterns) {
        return PatternMapper.INSTANCE.toDTO(competencyRankingPatterns);
    }

    /**
     * Support calculate pattern point
     *
     * @param competencyRankingPatternId
     * @return float value
     */
    public float calculateCRPatternPoint(int competencyRankingPatternId) {
        float total = 0;
        float levelRequirement, maxLevel, rankingWeight;
        CompetencyRankingPatterns competencyRankingPatterns = this.findById(competencyRankingPatternId);
        for (CompetencyRankingPatternDetails competencyRankingPatternDetail : competencyRankingPatterns.getCompetencyRankingPatternDetailsList()) {
            levelRequirement = competencyRankingPatternDetail.getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevel();
            maxLevel = competencyRankingPatternDetail.getRankingLevelRequirementPatterns().getMaxProficiencyLevel().getProficiencyLevel();
            rankingWeight = competencyRankingPatternDetail.getRankingWeight();
            total = total + levelRequirement * rankingWeight / maxLevel;
        }
        return total;
    }

    /**
     * Support calculate pattern ranking point
     *
     * @param competencyRankingPatterns
     * @return patten
     */
    public CompetencyRankingPatterns calculateAndSetRankingForCRPattern(CompetencyRankingPatterns competencyRankingPatterns) {
        float patternRankingPoint = this.calculateCRPatternPoint(competencyRankingPatterns.getCompetencyPatternId());
        if (patternRankingPoint <= 35) {
            competencyRankingPatterns.setJobRoles(jobRolesService.findById(1));
        } else if (patternRankingPoint <= 70 && patternRankingPoint >= 35) {
            competencyRankingPatterns.setJobRoles(jobRolesService.findById(2));
        } else {
            competencyRankingPatterns.setJobRoles(jobRolesService.findById(3));
        }
        return competencyRankingPatterns;
    }

    /**
     * Support copy pattern function
     *
     * @param competencyRankingPatterns
     * @return true or false
     */
    public boolean checkStatusToCopy(CompetencyRankingPatterns competencyRankingPatterns) {
        boolean statusCopy = true;
        int quantityOfPattenToCheckCopy = competencyRankingPatternsRepository.getAllByJobRoleAndDomain(competencyRankingPatterns.getJobRoles().getJobRoleName(), competencyRankingPatterns.getJobRoles().getDomains().getDomainName()).size();
        int maxQuantity = periodPatternRepository.findAll().size();
        if (quantityOfPattenToCheckCopy >= maxQuantity) {
            statusCopy = false;
        }
        return statusCopy;
    }
}
