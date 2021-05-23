package com.fa.training.mockproject.controllers;


import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.enumeric.EvidenceTypes;
import com.fa.training.mockproject.services.CompetencyComponentDetailService;
import com.fa.training.mockproject.services.CompetencyComponentsService;
import com.fa.training.mockproject.services.CompetencyRankingPatternDetailsService;
import com.fa.training.mockproject.services.CompetencyRankingPatternsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DivideWeightController {

    @Autowired
    private CompetencyComponentsService competencyComponentsService;

    @Autowired
    private CompetencyComponentDetailService competencyComponentDetailService;

    @Autowired
    private CompetencyRankingPatternsService competencyRankingPatternsService;

    @Autowired
    private CompetencyRankingPatternDetailsService competencyRankingPatternDetailsService;

    /**
     * Support divide weight for each component of pattern
     *
     * @param competencyRankingPatterns
     * @param componentIdList
     */
    public void supportDivideForComponentAndRankingPatternDetails(CompetencyRankingPatterns competencyRankingPatterns, List<Integer> componentIdList) {
        // Find all component by componentIDList
        List<CompetencyComponents> competencyComponentsListNeedAction = new ArrayList<>();
        int sumWeight = 0;
        for (Integer componentId : componentIdList) {
            CompetencyComponents competencyComponentsFromDB = competencyComponentsService.findById(componentId);
            sumWeight = sumWeight + competencyComponentsFromDB.getComponentWeight();
            competencyComponentsListNeedAction.add(competencyComponentsFromDB);
        }
        // Check size of list component of pattern
        int quantityComponentOfPattern = componentIdList.size();
        // -- Get quantity of component in database
        int maxQuantityOfComponent = competencyComponentsService.getAllCompetencyComponents().size();
        // If quantityComponentOfPattern match quantity of components in database --> use weight default else --> calculate weight again
        //=====================
        // Check competencyRankingPatterns is new or already exist in database
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = new ArrayList<>();
        if (competencyRankingPatterns.getCompetencyComponentsList() == null) {
            if (quantityComponentOfPattern == maxQuantityOfComponent) {
                for (CompetencyComponents components : competencyComponentsListNeedAction) {
                    int defaultWeightOfComponent = components.getComponentWeight();
                    int weightForEachComponentDetails = defaultWeightOfComponent / components.getCompetencyComponentDetailsList().size();
                    for (int i = 0; i <= components.getCompetencyComponentDetailsList().size() - 1; i++) {
                        CompetencyRankingPatternDetails competencyRankingPatternDetails = new CompetencyRankingPatternDetails();
                        RankingLevelRequirementPatterns rankingLevelRequirementPatterns = new RankingLevelRequirementPatterns();
                        //
                        competencyRankingPatternDetails.setCompetencyComponentDetails(components.getCompetencyComponentDetailsList().get(i));
                        competencyRankingPatternDetails.setCompetencyRankingPatterns(competencyRankingPatterns);
                        competencyRankingPatternDetails.setEvidenceTypes(EvidenceTypes.Optional);
                        competencyRankingPatternDetails.setRankingLevelRequirementPatterns(rankingLevelRequirementPatterns);
                        //
                        if (i == components.getCompetencyComponentDetailsList().size() - 1) {
                            int remainWeight = defaultWeightOfComponent - weightForEachComponentDetails * (components.getCompetencyComponentDetailsList().size() - 1);
                            competencyRankingPatternDetails.setRankingWeight((byte) remainWeight);
                        } else {
                            competencyRankingPatternDetails.setRankingWeight((byte) weightForEachComponentDetails);
                        }
                        competencyRankingPatternDetailsList.add(competencyRankingPatternDetails);
                    }
                }
            } else {
                int sumWeightReset = 0;
                int index = 0;
                for (CompetencyComponents components : competencyComponentsListNeedAction) {
                    int weightReset = (components.getComponentWeight() * 100) / sumWeight;
                    if (index == (competencyComponentsListNeedAction.size() - 1)) {
                        int weightForEachComponentDetails = (100 - sumWeightReset) / components.getCompetencyComponentDetailsList().size();
                        for (int i = 0; i <= components.getCompetencyComponentDetailsList().size() - 1; i++) {
                            CompetencyRankingPatternDetails competencyRankingPatternDetails = new CompetencyRankingPatternDetails();
                            RankingLevelRequirementPatterns rankingLevelRequirementPatterns = new RankingLevelRequirementPatterns();
                            //
                            competencyRankingPatternDetails.setCompetencyComponentDetails(components.getCompetencyComponentDetailsList().get(i));
                            competencyRankingPatternDetails.setCompetencyRankingPatterns(competencyRankingPatterns);
                            competencyRankingPatternDetails.setEvidenceTypes(EvidenceTypes.Optional);
                            competencyRankingPatternDetails.setRankingLevelRequirementPatterns(rankingLevelRequirementPatterns);
                            //
                            if (i == components.getCompetencyComponentDetailsList().size() - 1) {
                                int remainWeight = (100 - sumWeightReset) - (weightForEachComponentDetails * (components.getCompetencyComponentDetailsList().size() - 1));
                                competencyRankingPatternDetails.setRankingWeight((byte) remainWeight);
                            } else {
                                competencyRankingPatternDetails.setRankingWeight((byte) weightForEachComponentDetails);
                            }
                            competencyRankingPatternDetailsList.add(competencyRankingPatternDetails);
                        }
                    } else {
                        sumWeightReset = sumWeightReset + weightReset;
                        int weightForEachComponentDetails = weightReset / components.getCompetencyComponentDetailsList().size();
                        for (int i = 0; i <= components.getCompetencyComponentDetailsList().size() - 1; i++) {
                            CompetencyRankingPatternDetails competencyRankingPatternDetails = new CompetencyRankingPatternDetails();
                            RankingLevelRequirementPatterns rankingLevelRequirementPatterns = new RankingLevelRequirementPatterns();
                            //
                            competencyRankingPatternDetails.setCompetencyComponentDetails(components.getCompetencyComponentDetailsList().get(i));
                            competencyRankingPatternDetails.setCompetencyRankingPatterns(competencyRankingPatterns);
                            competencyRankingPatternDetails.setEvidenceTypes(EvidenceTypes.Optional);
                            competencyRankingPatternDetails.setRankingLevelRequirementPatterns(rankingLevelRequirementPatterns);
                            //
                            if (i == components.getCompetencyComponentDetailsList().size() - 1) {
                                int remainWeight = weightReset - weightForEachComponentDetails * (components.getCompetencyComponentDetailsList().size() - 1);
                                competencyRankingPatternDetails.setRankingWeight((byte) remainWeight);
                            } else {
                                competencyRankingPatternDetails.setRankingWeight((byte) weightForEachComponentDetails);
                            }
                            competencyRankingPatternDetailsList.add(competencyRankingPatternDetails);
                        }
                    }
                    index++;
                }
            }
        } else {
            // Find component that add/delete CompetencyRankingPatternDetails
            for (CompetencyComponents components : competencyComponentsListNeedAction) {
                int defaultWeightOfComponent = components.getComponentWeight();
                int weightForEachComponentDetails = defaultWeightOfComponent / components.getCompetencyComponentDetailsList().size();
                for (int i = 0; i <= components.getCompetencyComponentDetailsList().size() - 1; i++) {
                    CompetencyRankingPatternDetails competencyRankingPatternDetails = competencyRankingPatternDetailsService.findByComponentDetailsIdAndCompetencyRankingPatternId(components.getCompetencyComponentDetailsList().get(i).getComponentDetailId(), competencyRankingPatterns.getCompetencyPatternId());
                    //
                    if (i == components.getCompetencyComponentDetailsList().size() - 1) {
                        int remainWeight = defaultWeightOfComponent - weightForEachComponentDetails * (components.getCompetencyComponentDetailsList().size() - 1);
                        competencyRankingPatternDetails.setRankingWeight((byte) remainWeight);
                    } else {
                        competencyRankingPatternDetails.setRankingWeight((byte) weightForEachComponentDetails);
                    }
                    competencyRankingPatternDetailsService.save(competencyRankingPatternDetails);
                }
            }
        }
        //Set competencyRankingPatternDetailsList
        competencyRankingPatterns.setCompetencyRankingPatternDetailsList(competencyRankingPatternDetailsList);
        competencyRankingPatterns.setCompetencyComponentsList(competencyComponentsListNeedAction);
        competencyRankingPatternsService.save(competencyRankingPatterns);
    }

    /**
     * Delete competency ranking pattern details and calculate weight again
     *
     * @param competencyRankingPatternsId
     * @param componentDetailsId
     * @return
     */
    public boolean deleteAndUpdateWeightForCompetencyRankingPatternDetails(Integer competencyRankingPatternsId, Integer componentDetailsId) {
        boolean statusDelete;
        CompetencyRankingPatternDetails competencyRankingPatternDetails = competencyRankingPatternDetailsService.findByComponentDetailsIdAndCompetencyRankingPatternId(componentDetailsId, competencyRankingPatternsId);
        //
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = competencyRankingPatternDetailsService.findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(competencyRankingPatternDetails.getCompetencyComponentDetails().getCompetencyComponents().getComponentId(), competencyRankingPatternsId);

        int sumWeightOfComponent = 0;
        for (CompetencyRankingPatternDetails e : competencyRankingPatternDetailsList) {
            sumWeightOfComponent = sumWeightOfComponent + e.getRankingWeight();
        }
        if (competencyRankingPatternDetailsList.size() > 1) {
            competencyRankingPatternDetailsService.delete(competencyRankingPatternDetails);
            statusDelete = true;
        } else {
            statusDelete = false;
        }
        //
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsListAfterDelete = competencyRankingPatternDetailsService.findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(competencyRankingPatternDetails.getCompetencyComponentDetails().getCompetencyComponents().getComponentId(), competencyRankingPatternsId);
        int weightForEachComponentDetails = sumWeightOfComponent / competencyRankingPatternDetailsListAfterDelete.size();
        //
        for (int i = 0; i <= competencyRankingPatternDetailsListAfterDelete.size() - 1; i++) {
            CompetencyRankingPatternDetails competencyRankingPatternDetailsAfterDelete = competencyRankingPatternDetailsListAfterDelete.get(i);
            //
            if (i == competencyRankingPatternDetailsListAfterDelete.size() - 1) {
                int remainWeight = sumWeightOfComponent - weightForEachComponentDetails * (competencyRankingPatternDetailsListAfterDelete.size() - 1);
                competencyRankingPatternDetailsAfterDelete.setRankingWeight((byte) remainWeight);
            } else {
                competencyRankingPatternDetailsAfterDelete.setRankingWeight((byte) weightForEachComponentDetails);
            }
            competencyRankingPatternDetailsService.save(competencyRankingPatternDetailsAfterDelete);
        }
        return statusDelete;
    }

    /**
     * Support add competency ranking pattern details and calculate weight again
     *
     * @param competencyRankingPatternsId
     * @param componentDetailsId
     */
    public void addAndUpdateWeightForCompetencyRankingPatternDetails(Integer competencyRankingPatternsId, Integer componentDetailsId) {
        boolean addStatus;
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyRankingPatternsId);
        CompetencyComponentDetails competencyComponentDetails = competencyComponentDetailService.findCompetencyComponentDetailsById(componentDetailsId);
        // Phai ranking pattern details
        CompetencyRankingPatternDetails competencyRankingPatternDetails = new CompetencyRankingPatternDetails();
        competencyRankingPatternDetails.setCompetencyRankingPatterns(competencyRankingPatterns);
        competencyRankingPatternDetails.setCompetencyComponentDetails(competencyComponentDetails);
        competencyRankingPatternDetails.setEvidenceTypes(EvidenceTypes.Optional);
        // Phai di tao mot cai moi cua ranking level requirement pattern
        RankingLevelRequirementPatterns rankingLevelRequirementPatterns = new RankingLevelRequirementPatterns();
        competencyRankingPatternDetails.setRankingLevelRequirementPatterns(rankingLevelRequirementPatterns);
        // Save competencyRankingPatternDetails
        competencyRankingPatternDetailsService.save(competencyRankingPatternDetails);
        // Phan chia lai component weight
        //
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = competencyRankingPatternDetailsService.findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(competencyComponentDetails.getCompetencyComponents().getComponentId(), competencyRankingPatternsId);
        int sumWeightOfComponent = 0;
        for (CompetencyRankingPatternDetails e : competencyRankingPatternDetailsList) {
            sumWeightOfComponent = sumWeightOfComponent + e.getRankingWeight();
        }
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsListAfterAdd = competencyRankingPatternDetailsService.findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(competencyComponentDetails.getCompetencyComponents().getComponentId(), competencyRankingPatternsId);
        int weightForEachComponentDetails = sumWeightOfComponent / competencyRankingPatternDetailsListAfterAdd.size();
        //
        for (int i = 0; i <= competencyRankingPatternDetailsListAfterAdd.size() - 1; i++) {
            CompetencyRankingPatternDetails competencyRankingPatternDetailsAfterDelete = competencyRankingPatternDetailsListAfterAdd.get(i);
            //
            if (i == competencyRankingPatternDetailsListAfterAdd.size() - 1) {
                int remainWeight = sumWeightOfComponent - weightForEachComponentDetails * (competencyRankingPatternDetailsListAfterAdd.size() - 1);
                competencyRankingPatternDetailsAfterDelete.setRankingWeight((byte) remainWeight);
            } else {
                competencyRankingPatternDetailsAfterDelete.setRankingWeight((byte) weightForEachComponentDetails);
            }
            competencyRankingPatternDetailsService.save(competencyRankingPatternDetailsAfterDelete);
        }
    }
}
