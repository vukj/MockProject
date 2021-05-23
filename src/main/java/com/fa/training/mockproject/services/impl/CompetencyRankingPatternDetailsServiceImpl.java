package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.CompetencyComponents;
import com.fa.training.mockproject.entities.CompetencyRankingPatternDetails;
import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.entities.dto.PatternSummaryDTO;
import com.fa.training.mockproject.repositories.CompetencyRankingPatternDetailsRepository;
import com.fa.training.mockproject.services.CompetencyRankingPatternDetailsService;
import com.fa.training.mockproject.services.CompetencyRankingPatternsService;
import com.fa.training.mockproject.services.ProficiencyLevelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CompetencyRankingPatternDetailsServiceImpl implements CompetencyRankingPatternDetailsService {

    @Autowired
    private CompetencyRankingPatternDetailsService competencyRankingPatternDetailsService;

    @Autowired
    private ProficiencyLevelsService proficiencyLevelsService;

    @Autowired
    private CompetencyRankingPatternDetailsRepository competencyRankingPatternDetailsRepository;

    @Autowired
    private CompetencyRankingPatternsService competencyRankingPatternsService;

    /**
     * Save competency ranking pattern details
     *
     * @param competencyRankingPatternDetails
     */
    @Override
    public void save(CompetencyRankingPatternDetails competencyRankingPatternDetails) {
        competencyRankingPatternDetailsRepository.save(competencyRankingPatternDetails);
    }

    /**
     * Find competency ranking pattern details by id
     *
     * @param id
     * @return competency ranking pattern details
     */
    @Override
    public CompetencyRankingPatternDetails findById(int id) {
        return competencyRankingPatternDetailsRepository.findByCompetencyPatternDetailId(id);
    }

    /**
     * Get all competency ranking pattern details
     *
     * @param id
     * @return list of competency ranking pattern details
     */
    @Override
    public List<CompetencyRankingPatternDetails> findAllByCompetencyRankingPatternId(int id) {
        return competencyRankingPatternDetailsRepository.findAllByCompetencyRankingPatterns_CompetencyPatternId(id);
    }

    /**
     * Update competency ranking pattern details
     *
     * @param competencyRankingPatternDetails
     */
    @Override
    public void update(CompetencyRankingPatternDetails competencyRankingPatternDetails) {
        competencyRankingPatternDetailsRepository.save(competencyRankingPatternDetails);
    }

    /**
     * Find competency ranking pattern details by id
     *
     * @param id
     * @return competency ranking pattern details
     */
    @Override
    public CompetencyRankingPatternDetails findByCompetencyComponentDetailsId(int id) {
        return competencyRankingPatternDetailsRepository.findByCompetencyComponentDetails_ComponentDetailId(id);
    }

    /**
     * Find competency ranking pattern details base on it's id and pattern's Id
     *
     * @param componentDetailsId
     * @param competencyRankingPatternId
     * @return competency ranking pattern details
     */
    @Override
    public CompetencyRankingPatternDetails findByComponentDetailsIdAndCompetencyRankingPatternId(int componentDetailsId, int competencyRankingPatternId) {
        return competencyRankingPatternDetailsRepository.findCompetencyRankingPatternDetailsByCompetencyComponentDetails_ComponentDetailIdAndCompetencyRankingPatterns_CompetencyPatternId(componentDetailsId, competencyRankingPatternId);
    }

    /**
     * Get all competency ranking pattern details base on component's Id and pattern's Id
     *
     * @param componentId
     * @param patternId
     * @return list of competency ranking pattern details
     */
    @Override
    public List<CompetencyRankingPatternDetails> findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(int componentId, int patternId) {
        return competencyRankingPatternDetailsRepository.findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(componentId, patternId);
    }

    /**
     * Delete competency ranking pattern details
     *
     * @param competencyRankingPatternDetails
     */
    @Override
    public void delete(CompetencyRankingPatternDetails competencyRankingPatternDetails) {
        competencyRankingPatternDetailsRepository.delete(competencyRankingPatternDetails);
    }

    /**
     * Support calculate point, percent for component, set evidence status and check status components
     *
     * @param competencyRankingPatterns
     * @return list of PatternSummaryDTO
     */
    @Override
    public List<PatternSummaryDTO> calculateAndShowInformationForAllComponent(CompetencyRankingPatterns competencyRankingPatterns) {
        List<PatternSummaryDTO> patternSummaryDTOList = new ArrayList<>();
        //
        List<CompetencyComponents> competencyComponentsList = competencyRankingPatterns.getCompetencyComponentsList();
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = competencyRankingPatternDetailsService.findAllByCompetencyRankingPatternId(competencyRankingPatterns.getCompetencyPatternId());
        //
        Map<Integer, Integer> competencyComponentsPercent = new HashMap<>();
        Map<Integer, String> competencyComponentsEvidenceStatus = new HashMap<>();
        Map<Integer, Double> competencyComponentsBasePoint = new HashMap<>();
        Map<Integer, String> competencyComponentsStatus = new HashMap<>();
        // Check and set evidence status for component
        for (CompetencyComponents competencyComponents : competencyComponentsList) {
            competencyComponentsEvidenceStatus.put(competencyComponents.getComponentId(), "Optional");
            String evidenceStatus = "Optional";
            for (CompetencyRankingPatternDetails CRDP : competencyRankingPatternDetailsList) {
                if (CRDP.getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == competencyComponents.getComponentId()) {
                    if (CRDP.getEvidenceTypes().toString().equals("Required")) {
                        evidenceStatus = "Required";
                        break;
                    }
                }
            }
            competencyComponentsEvidenceStatus.put(competencyComponents.getComponentId(), evidenceStatus);
        }
        // Check and set completed status for component
        for (CompetencyComponents competencyComponents : competencyComponentsList) {
            competencyComponentsStatus.put(competencyComponents.getComponentId(), "Completed");
            String status = "Completed";
            for (CompetencyRankingPatternDetails CRDP : competencyRankingPatternDetailsList) {
                if (CRDP.getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == competencyComponents.getComponentId()) {
                    if (CRDP.getRankingLevelRequirementPatterns().getProficiencyLevels() == null) {
                        status = "Not completed";
                        break;
                    }
                }
            }
            competencyComponentsStatus.put(competencyComponents.getComponentId(), status);
        }
        // Support calculate weight and base point for each component
        for (CompetencyComponents competencyComponents : competencyComponentsList) {
            //
            competencyComponentsPercent.put(competencyComponents.getComponentId(), 0);
            int updateValueWeight = competencyComponentsPercent.get(competencyComponents.getComponentId());
            //
            competencyComponentsBasePoint.put(competencyComponents.getComponentId(), 0.0);
            double updateValueBasePoint = competencyComponentsBasePoint.get(competencyComponents.getComponentId());
            //
            for (CompetencyRankingPatternDetails CRDP : competencyRankingPatternDetailsList) {
                // Calculate ranking weight
                if (CRDP.getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == competencyComponents.getComponentId()) {
                    updateValueWeight = updateValueWeight + CRDP.getRankingWeight();
                }
                // Calculate base point and check status
                if (CRDP.getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == competencyComponents.getComponentId()) {
                    int maxLevelOfCompetencyComponentDetails = CRDP.getCompetencyComponentDetails().getProficiencyLevelTypes().getProficiencyLevelsList().get(CRDP.getCompetencyComponentDetails().getProficiencyLevelTypes().getProficiencyLevelsList().size() - 1).getProficiencyLevel();
                    int currentLevel = 0;
                    if (CRDP.getRankingLevelRequirementPatterns().getProficiencyLevels() != null) {
                        currentLevel = proficiencyLevelsService.findProficiencyLevelsById(CRDP.getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevelId()).getProficiencyLevel();
                    }
                    double pointOfCompetencyRankingPatternDetails = (currentLevel * CRDP.getRankingWeight() * 1.0) / maxLevelOfCompetencyComponentDetails;
                    updateValueBasePoint = updateValueBasePoint + pointOfCompetencyRankingPatternDetails;
                }
            }
            competencyComponentsPercent.put(competencyComponents.getComponentId(), updateValueWeight);
            competencyComponentsBasePoint.put(competencyComponents.getComponentId(), updateValueBasePoint);
        }
        float sumRakingPointPattern = 0;
        for (CompetencyComponents e : competencyComponentsList) {
            PatternSummaryDTO patternSummaryDTO = new PatternSummaryDTO();
            patternSummaryDTO.setCompetencyComponents(e);
            patternSummaryDTO.setEvidence(competencyComponentsEvidenceStatus.get(e.getComponentId()));
            patternSummaryDTO.setTotalWeight(competencyComponentsPercent.get(e.getComponentId()).toString());
            patternSummaryDTO.setBasePoint(competencyComponentsBasePoint.get(e.getComponentId()).toString());
            patternSummaryDTO.setStatus(competencyComponentsStatus.get(e.getComponentId()));
            patternSummaryDTOList.add(patternSummaryDTO);
            sumRakingPointPattern = Float.valueOf(String.format("%.2f", (float) (sumRakingPointPattern + competencyComponentsBasePoint.get(e.getComponentId()))));
        }
        competencyRankingPatterns.setComponentWeight(sumRakingPointPattern);
        competencyRankingPatternsService.save(competencyRankingPatterns);
        return patternSummaryDTOList;
    }

}
