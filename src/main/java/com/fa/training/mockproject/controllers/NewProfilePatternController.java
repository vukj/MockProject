package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.entities.dto.CompetencyRankingPatternDetailsDTO;
import com.fa.training.mockproject.entities.dto.PatternSummaryDTO;
import com.fa.training.mockproject.entities.dto.RankingPatternDetailsListWrapper;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.services.*;
import com.fa.training.mockproject.services.impl.CompetencyRankingProfilesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/profile-pattern/new-profile-pattern")
@SessionAttributes("user")
public class NewProfilePatternController {

    @Autowired
    private CompetencyRankingPatternsService competencyRankingPatternsService;

    @Autowired
    private CompetencyComponentsService competencyComponentsService;

    @Autowired
    private CompetencyRankingPatternDetailsService competencyRankingPatternDetailsService;

    @Autowired
    private CompetencyComponentDetailService competencyComponentDetailService;

    @Autowired
    private ProficiencyLevelsService proficiencyLevelsService;

    @Autowired
    private CompetencyRankingProfilesServiceImpl competencyRankingProfilesService;

    @Autowired
    private DivideWeightController divideWeightController;

    //===================COMPONENT DETAILS PAGE -- SHOW DETAILS==================================

    /**
     * Show all details for each component
     *
     * @param competencyRankingPatternsId
     * @param componentId
     * @param rankingPatternDetailsListWrapper
     * @param pageIndex
     * @param errorDeletePatternDetails
     * @return
     */
    @GetMapping("{competencyRankingPatternsId}/{componentId}/{pageIndex}")
    public ModelAndView gotoIndexPage(
            @PathVariable("competencyRankingPatternsId") int competencyRankingPatternsId,
            @PathVariable("componentId") int componentId,
            @ModelAttribute("rankingPatternDetailsListWrapper") RankingPatternDetailsListWrapper rankingPatternDetailsListWrapper,
            @PathVariable Integer pageIndex,
            @RequestParam(value = "errorDeletePatternDetails", defaultValue = "false") String errorDeletePatternDetails) {
        ModelAndView modelAndView = new ModelAndView();
        //
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyRankingPatternsId);
        //
        int pageSize = competencyRankingPatterns.getCompetencyComponentsList().size();
        //
        if (pageIndex <= pageSize) {
            CompetencyComponents components = competencyComponentsService.findById(componentId);
            //
            List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = competencyRankingPatternDetailsService.findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(components.getComponentId(), competencyRankingPatternsId);
            //
            rankingPatternDetailsListWrapper.setCompetencyRankingPatternDetailsList(competencyRankingPatternDetailsList);
            // Check status component is completed or not completed --> to change ball's color
            List<PatternSummaryDTO> patternSummaryDTOList = competencyRankingPatternDetailsService.calculateAndShowInformationForAllComponent(competencyRankingPatternsService.findById(competencyRankingPatternsId));
            //
            modelAndView.addObject("competencyRankingPatterns", competencyRankingPatterns);
            modelAndView.addObject("rankingPatternDetailsListWrapper", rankingPatternDetailsListWrapper);
            modelAndView.addObject("pageIndex", pageIndex);
            modelAndView.addObject("patternSummaryDTOList", patternSummaryDTOList);
            modelAndView.addObject("errorDeletePatternDetails", errorDeletePatternDetails);

            // Add function to add component details
            List<CompetencyComponentDetails> competencyComponentDetailsListRemain = new ArrayList<>();
            List<String> competencyRankingPatternDetailsName = new ArrayList<>();
            for (CompetencyRankingPatternDetails e : competencyRankingPatternDetailsList) {
                competencyRankingPatternDetailsName.add(e.getCompetencyComponentDetails().getComponentDetailName());
            }
            // Check CompetencyRankingPatternDetails is not exist --> add to competencyComponentDetailsListRemain
            if (competencyRankingPatternDetailsList.size() < components.getCompetencyComponentDetailsList().size()) {
                for (CompetencyComponentDetails e : components.getCompetencyComponentDetailsList()) {
                    if (!competencyRankingPatternDetailsName.contains(e.getComponentDetailName())) {
//                        System.out.println(e.getComponentDetailName());
                        competencyComponentDetailsListRemain.add(e);
                    }
                }
            }
            modelAndView.addObject("competencyComponentDetailsListRemain", competencyComponentDetailsListRemain);
            modelAndView.setViewName("newprofilepattern/index");
        } else {
            modelAndView.setViewName("redirect:profile-summary" + '/' + competencyRankingPatternsId);
        }
        return modelAndView;
    }

    //===================COMPONENT DETAILS PAGE -- SAVE DETAILS==================================

    /**
     * Save competency component
     *
     * @param rankingPatternDetailsListWrapper
     * @param levelList
     * @param componentDetailIdList
     * @param patternId
     * @param pageIndex
     * @return
     */
    @PostMapping("save-component")
    public ModelAndView saveRankingPatternDetails(
            @ModelAttribute("rankingPatternDetailsListWrapper") RankingPatternDetailsListWrapper rankingPatternDetailsListWrapper,
            @RequestParam("level") List<Integer> levelList,
            @RequestParam("componentDetailId") List<Integer> componentDetailIdList,
            @RequestParam("patternId") int patternId,
            @RequestParam("pageIndex") int pageIndex) {

        ModelAndView modelAndView = new ModelAndView();
        // Find pattern
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(patternId);
        // Get id from Component of pattern
        List<CompetencyComponents> competencyComponentsList = competencyRankingPatterns.getCompetencyComponentsList();
        List<Integer> componentsIdList = new ArrayList<>();
        for (CompetencyComponents components : competencyComponentsList) {
            componentsIdList.add(components.getComponentId());
        }
        // Get proficiencyLevels
        List<ProficiencyLevels> proficiencyLevelsList = new ArrayList<>();
        for (Integer i : levelList) {
            proficiencyLevelsList.add(proficiencyLevelsService.findProficiencyLevelsById(i));
        }
        // Find component details by id
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsListFromDB = new ArrayList<>();
        for (Integer i : componentDetailIdList) {
            competencyRankingPatternDetailsListFromDB.add(competencyRankingPatternDetailsService.findById(i));
        }
        //
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = rankingPatternDetailsListWrapper.getCompetencyRankingPatternDetailsList();
        //
        int index = 0;
        for (CompetencyRankingPatternDetails details : competencyRankingPatternDetailsList) {
            //
            CompetencyRankingPatternDetails competencyRankingPatternDetails = competencyRankingPatternDetailsListFromDB.get(index);
            competencyRankingPatternDetails.setEvidenceTypes(details.getEvidenceTypes());
            //
            RankingLevelRequirementPatterns rankingLevelRequirementPatterns = competencyRankingPatternDetails.getRankingLevelRequirementPatterns();
            rankingLevelRequirementPatterns.setProficiencyLevels(proficiencyLevelsList.get(index));
            competencyRankingPatternDetails.setRankingLevelRequirementPatterns(rankingLevelRequirementPatterns);
            competencyRankingPatternDetailsService.save(competencyRankingPatternDetails);
            index++;
        }
        if (pageIndex < componentsIdList.size()) {
            modelAndView.setViewName("redirect:" + patternId + '/' + componentsIdList.get(pageIndex) + '/' + (pageIndex + 1));
        } else {
            modelAndView.setViewName("redirect:profile-summary" + '/' + patternId);
        }
        return modelAndView;
    }

    //===================COMPONENT DETAILS PAGE -- SAVE AND FINISH==================================

    /**
     * Save competency component and jump to summary page
     *
     * @param rankingPatternDetailsListWrapper
     * @param levelList
     * @param componentDetailIdList
     * @param patternId
     * @return
     */
    @PostMapping("save-component-finish")
    public ModelAndView saveRankingPatternDetailsAndGoToSummaryPage(
            @ModelAttribute("rankingPatternDetailsListWrapper") RankingPatternDetailsListWrapper rankingPatternDetailsListWrapper,
            @RequestParam("level") List<Integer> levelList,
            @RequestParam("componentDetailId") List<Integer> componentDetailIdList,
            @RequestParam("patternId") int patternId) {

        ModelAndView modelAndView = new ModelAndView();
        // Find pattern
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(patternId);
        // Get id from Component of pattern
        List<CompetencyComponents> competencyComponentsList = competencyRankingPatterns.getCompetencyComponentsList();
        List<Integer> componentsIdList = new ArrayList<>();
        for (CompetencyComponents components : competencyComponentsList) {
            componentsIdList.add(components.getComponentId());
        }
        // Get proficiencyLevels
        List<ProficiencyLevels> proficiencyLevelsList = new ArrayList<>();
        for (Integer i : levelList) {
            proficiencyLevelsList.add(proficiencyLevelsService.findProficiencyLevelsById(i));
        }
        // Find component details by id
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsListFromDB = new ArrayList<>();
        for (Integer i : componentDetailIdList) {
            competencyRankingPatternDetailsListFromDB.add(competencyRankingPatternDetailsService.findById(i));
        }
        //
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = rankingPatternDetailsListWrapper.getCompetencyRankingPatternDetailsList();
        //
        int index = 0;
        for (CompetencyRankingPatternDetails details : competencyRankingPatternDetailsList) {
            //
            CompetencyRankingPatternDetails competencyRankingPatternDetails = competencyRankingPatternDetailsListFromDB.get(index);
            competencyRankingPatternDetails.setEvidenceTypes(details.getEvidenceTypes());
            //
            RankingLevelRequirementPatterns rankingLevelRequirementPatterns = competencyRankingPatternDetails.getRankingLevelRequirementPatterns();
            rankingLevelRequirementPatterns.setProficiencyLevels(proficiencyLevelsList.get(index));
            competencyRankingPatternDetails.setRankingLevelRequirementPatterns(rankingLevelRequirementPatterns);
            competencyRankingPatternDetailsService.save(competencyRankingPatternDetails);
            index++;
        }
        modelAndView.setViewName("redirect:profile-summary" + '/' + patternId);
        return modelAndView;
    }


    //===================COMPONENT DETAILS PAGE -- DELETE DETAILS==================================

    /**
     * Delete competency component details
     *
     * @param competencyRankingPatternsId
     * @param componentId
     * @param componentDetailsId
     * @param pageIndex
     * @return
     */
    @GetMapping("deletePatternDetails/{competencyRankingPatternsId}/{componentId}/{componentDetailsId}/{pageIndex}")
    public ModelAndView deleteRankingPatternDetails(
            @PathVariable("competencyRankingPatternsId") Integer competencyRankingPatternsId,
            @PathVariable("componentId") Integer componentId,
            @PathVariable("componentDetailsId") Integer componentDetailsId,
            @PathVariable("pageIndex") Integer pageIndex) {
        ModelAndView modelAndView = new ModelAndView();
        boolean statusDelete = divideWeightController.deleteAndUpdateWeightForCompetencyRankingPatternDetails(competencyRankingPatternsId, componentDetailsId);
        if (!statusDelete) {
            modelAndView.addObject("errorDeletePatternDetails", "true");
        }
        modelAndView.setViewName("redirect:/profile-pattern/new-profile-pattern/" + competencyRankingPatternsId + "/" + componentId + "/" + pageIndex);
        return modelAndView;
    }

    //===================COMPONENT DETAILS PAGE -- ADD DETAILS==================================

    /**
     * Add competency component details
     *
     * @param competencyRankingPatternsId
     * @param pageIndex
     * @param componentDetailsId
     * @return
     */
    @PostMapping("/add-component-details-remain")
    public ModelAndView addComponentDetails(
            @RequestParam("patternId") int competencyRankingPatternsId,
            @RequestParam("pageIndex") int pageIndex,
            @RequestParam("componentDetailsRemain") Integer componentDetailsId) {
        ModelAndView modelAndView = new ModelAndView();
        CompetencyComponents components = competencyComponentDetailService.findCompetencyComponentDetailsById(componentDetailsId).getCompetencyComponents();
        divideWeightController.addAndUpdateWeightForCompetencyRankingPatternDetails(competencyRankingPatternsId, componentDetailsId);
        modelAndView.setViewName("redirect:/profile-pattern/new-profile-pattern/" + competencyRankingPatternsId + "/" + components.getComponentId() + "/" + pageIndex);
        return modelAndView;
    }


    //==================SUMMARY PAGE -- SUPPORT SHOW CHART==================

    /**
     * Get all list of PatternSummaryDTO
     *
     * @param patternId
     * @return
     */
    @GetMapping("json-pattern-summary/{patternId}")
    @ResponseBody
    public ResponseEntity<List<PatternSummaryDTO>> getPatternSummaryDTO(@PathVariable("patternId") Integer patternId) {
        List<PatternSummaryDTO> patternSummaryDTOList = competencyRankingPatternDetailsService.calculateAndShowInformationForAllComponent(competencyRankingPatternsService.findById(patternId));
        return new ResponseEntity<>(patternSummaryDTOList, HttpStatus.OK);
    }

    //==================SUMMARY PAGE - SHOW COMPONENTS===============

    /**
     * Show all information
     *
     * @param competencyRankingPatternsId
     * @return
     */
    @GetMapping("profile-summary/{competencyRankingPatternsId}")
    public ModelAndView goToProfileSummaryPage(@PathVariable("competencyRankingPatternsId") Integer competencyRankingPatternsId) {
        ModelAndView modelAndView = new ModelAndView();
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyRankingPatternsId);
        List<CompetencyComponents> competencyComponentsList = competencyRankingPatterns.getCompetencyComponentsList();
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = competencyRankingPatternDetailsService.findAllByCompetencyRankingPatternId(competencyRankingPatternsId);
        // Support check pattern is completed or not
        boolean checkPatternIsCompete = true;
        for (CompetencyRankingPatternDetails e : competencyRankingPatternDetailsList) {
            if (e.getRankingLevelRequirementPatterns().getProficiencyLevels() == null) {
                checkPatternIsCompete = false;
                break;
            }
        }

        if ((checkPatternIsCompete == true) && (competencyRankingPatterns.getActiveStatus().toString() != "Activated")) {
            competencyRankingPatterns.setActiveStatus(ActiveStatus.Inactive);
            competencyRankingPatternsService.save(competencyRankingPatterns);
        }
        // Support jump to the components is not completed
        List<PatternSummaryDTO> patternSummaryDTOList = competencyRankingPatternDetailsService.calculateAndShowInformationForAllComponent(competencyRankingPatterns);
        float totalRequirePoint = 0;
        int totalCComponentPoint = 0;
        for (PatternSummaryDTO e : patternSummaryDTOList) {
            if (!e.getStatus().equals("Completed")) {
                modelAndView.addObject("componentIdNotCompleteAtFirst", e.getCompetencyComponents().getComponentId());
                break;
            }
            totalRequirePoint += Float.parseFloat(e.getBasePoint());
            totalCComponentPoint += Integer.parseInt(e.getTotalWeight());
        }
        modelAndView.addObject("totalRequirePoint", competencyRankingProfilesService.formatNicelyFloat(totalRequirePoint));
        modelAndView.addObject("totalCComponentPoint", competencyRankingProfilesService.formatNicelyFloat(totalCComponentPoint));
        modelAndView.addObject("competencyRankingPatterns", competencyRankingPatterns);
        modelAndView.addObject("competencyComponentsList", competencyComponentsList);
        modelAndView.addObject("patternSummaryDTOList", patternSummaryDTOList);

        modelAndView.setViewName("newprofilepattern/profilepatternsummary");
        return modelAndView;
    }

    //===============SUMMARY PAGE -- APPROVE / DISAPPROVE PATTERN=================

    /**
     * Support approve pattern
     *
     * @param competencyRankingPatternsId
     * @return
     */
    @GetMapping("approve/{competencyRankingPatternsId}")
    public ModelAndView setApprovePatternAnReturnIndexPage(@PathVariable("competencyRankingPatternsId") Integer competencyRankingPatternsId) {
        ModelAndView modelAndView = new ModelAndView();
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyRankingPatternsId);
        competencyRankingPatterns.setActiveStatus(ActiveStatus.Activated);
        competencyRankingPatternsService.save(competencyRankingPatterns);
        modelAndView.addObject("fieldDisabled", "disabled");
        modelAndView.setViewName("redirect:/profile-pattern");
        return modelAndView;
    }

    /**
     * Support disapprove pattern
     *
     * @param competencyRankingPatternsId
     * @return
     */
    @GetMapping("disapprove/{competencyRankingPatternsId}")
    public ModelAndView setDisApprovePatternAnReturnIndexPage(@PathVariable("competencyRankingPatternsId") Integer competencyRankingPatternsId) {
        ModelAndView modelAndView = new ModelAndView();
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyRankingPatternsId);
        competencyRankingPatterns.setActiveStatus(ActiveStatus.Inactive);
        competencyRankingPatternsService.save(competencyRankingPatterns);
        modelAndView.addObject("fieldDisabled", "disabled");
        modelAndView.setViewName("redirect:/profile-pattern");
        return modelAndView;
    }

    //================SUMMARY PAGE --SHOW DETAILS ================

    /**
     * Support show details for each component
     *
     * @param patternId
     * @param componentName
     * @return
     */
    @GetMapping("component-details/{patternId}/{componentName}")
    @ResponseBody
    public ResponseEntity<List<CompetencyRankingPatternDetailsDTO>> getCompetencyRankingPatternDetails(@PathVariable("patternId") Integer patternId, @PathVariable("componentName") String componentName) {
        CompetencyComponents components = competencyComponentsService.findByComponentName(componentName);
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsList = competencyRankingPatternDetailsService.findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(components.getComponentId(), patternId);
        List<CompetencyRankingPatternDetailsDTO> competencyRankingPatternDetailsDTOList = new ArrayList<>();
        for (CompetencyRankingPatternDetails e : competencyRankingPatternDetailsList) {
            //
            CompetencyRankingPatternDetailsDTO competencyRankingPatternDetailsDTO = new CompetencyRankingPatternDetailsDTO();
            // Calculate point for details
            int maxLevelOfCompetencyComponentDetails = e.getCompetencyComponentDetails().getProficiencyLevelTypes().getProficiencyLevelsList().get(e.getCompetencyComponentDetails().getProficiencyLevelTypes().getProficiencyLevelsList().size() - 1).getProficiencyLevel();
            int currentLevel = 0;
            if (e.getRankingLevelRequirementPatterns().getProficiencyLevels() != null) {
                currentLevel = proficiencyLevelsService.findProficiencyLevelsById(e.getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevelId()).getProficiencyLevel();
                double pointOfCompetencyRankingPatternDetails = (currentLevel * e.getRankingWeight() * 1.0) / maxLevelOfCompetencyComponentDetails;
                competencyRankingPatternDetailsDTO.setPointDetails(String.format("%,.1f", pointOfCompetencyRankingPatternDetails));
            } else {
                competencyRankingPatternDetailsDTO.setPointDetails("--");
            }

            competencyRankingPatternDetailsDTO.setCompetencyPatternDetailId(e.getCompetencyPatternDetailId());
            competencyRankingPatternDetailsDTO.setRankingWeight(e.getRankingWeight());
            competencyRankingPatternDetailsDTO.setEvidenceTypes(e.getEvidenceTypes());
            competencyRankingPatternDetailsDTO.setCompetencyComponentDetails(e.getCompetencyComponentDetails());
            competencyRankingPatternDetailsDTO.setRankingLevelRequirementPatterns(e.getRankingLevelRequirementPatterns());
            //

            //
            competencyRankingPatternDetailsDTOList.add(competencyRankingPatternDetailsDTO);
        }
        return new ResponseEntity<>(competencyRankingPatternDetailsDTOList, HttpStatus.OK);
    }

    // ======================= SAVE USER ON SESSION =======================
    @ModelAttribute
    public UserAccountsLoginDTO user(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        return userAccountsLoginDTO;
    }


}
