package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.entities.CompetencyRankingProfileDetails;
import com.fa.training.mockproject.entities.DataSources;
import com.fa.training.mockproject.entities.ProficiencyLevels;
import com.fa.training.mockproject.entities.dto.ProfileDTO;
import com.fa.training.mockproject.entities.dto.ProfileDetailWrapperList;
import com.fa.training.mockproject.entities.dto.ProfileSummaryDTO;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.enumeric.StatusTypes;
import com.fa.training.mockproject.services.*;
import com.fa.training.mockproject.services.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/new-profile")
@SessionAttributes(value = {"user", "competencyRankingProfiles", "lastModifiedProfile"})
public class NewProfileController {

    @Autowired
    private CompetencyRankingProfilesService competencyRankingProfilesService;

    @Autowired
    private CompetencyRankingPatternsService competencyRankingPatternsService;

    @Autowired
    private CompetencyRankingProfileDetailsService competencyRankingProfileDetailsService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private ProficiencyLevelsService proficiencyLevelsService;

    /**
     * Create new profile
     *
     * @param profileDTO
     * @return
     */
    @PostMapping
    public String saveProfile(@RequestParam(value = "competencyPatternId", required = false) Integer competencyPatternId,
                              @ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO,
                              @ModelAttribute("profileDTO") ProfileDTO profileDTO,
                              Model model) {
        if (competencyPatternId != null) {
            CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyPatternId);
            profileDTO.setCompetencyRankingPatterns(competencyRankingPatterns);
            profileDTO.setEmployees(userAccountsLoginDTO.getEmployees());
            profileDTO.setJobRoles(competencyRankingPatterns.getJobRoles());
            profileDTO.setStatusTypes(StatusTypes.InProcess);
            profileDTO.setSelfRakingPoints("0");
            competencyRankingProfilesService.save(profileDTO);
            competencyRankingPatterns.getCompetencyRankingPatternDetailsList().forEach(competencyRankingPatternDetails -> {
                ProfileDTO profile = competencyRankingProfilesService.findFirstByEmployees_EmployeeIdOrderByModifiedDesc(userAccountsLoginDTO.getEmployees().getEmployeeId());
                CompetencyRankingProfileDetails competencyRankingProfileDetails = new CompetencyRankingProfileDetails();
                competencyRankingProfileDetails.setCompetencyRankingPatternDetails(competencyRankingPatternDetails);
                competencyRankingProfileDetails.setCompetencyRankingProfiles(ProfileMapper.INSTANCE.fromDTO(profile));
                competencyRankingProfileDetailsService.save(competencyRankingProfileDetails);
            });
            ProfileDTO newProfileDTOFromDB = competencyRankingProfilesService.findFirstByEmployees_EmployeeIdOrderByModifiedDesc(userAccountsLoginDTO.getEmployees().getEmployeeId());
            model.addAttribute("competencyRankingProfiles", newProfileDTOFromDB);
//        Copy from an exits Profile
        } else {
            ProfileDTO profileDTOFromDB = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesService.findById(profileDTO.getRankingProfileId()));
            ProfileDTO newProfile = new ProfileDTO();
            newProfile.setCompetencyRankingPatterns(profileDTOFromDB.getCompetencyRankingPatterns());
            newProfile.setProfileTitle(profileDTO.getProfileTitle());
            newProfile.setEmployees(profileDTOFromDB.getEmployees());
            newProfile.setJobRoles(profileDTOFromDB.getCompetencyRankingPatterns().getJobRoles());
            newProfile.setSelfRakingPoints(profileDTOFromDB.getSelfRakingPoints());
            newProfile.setStatusTypes(StatusTypes.InProcess);
            competencyRankingProfilesService.save(newProfile);
            profileDTOFromDB.getCompetencyRankingProfileDetailsList().forEach(competencyRankingProfileDetails -> {
                ProfileDTO profileFromDB = competencyRankingProfilesService.findFirstByEmployees_EmployeeIdOrderByModifiedDesc(userAccountsLoginDTO.getEmployees().getEmployeeId());
                CompetencyRankingProfileDetails newProfileDetails = new CompetencyRankingProfileDetails();
                newProfileDetails.setCompetencyRankingPatternDetails(competencyRankingProfileDetails.getCompetencyRankingPatternDetails());
                newProfileDetails.setDataSources(competencyRankingProfileDetails.getDataSources());
                newProfileDetails.setProficiencyLevels(competencyRankingProfileDetails.getProficiencyLevels());
                newProfileDetails.setSelfRankingNotes(competencyRankingProfileDetails.getSelfRankingNotes());
                newProfileDetails.setCompetencyRankingProfiles(ProfileMapper.INSTANCE.fromDTO(profileFromDB));
                competencyRankingProfileDetailsService.save(newProfileDetails);
            });
            ProfileDTO newProfileDTOFromDB = competencyRankingProfilesService.findFirstByEmployees_EmployeeIdOrderByModifiedDesc(userAccountsLoginDTO.getEmployees().getEmployeeId());
            model.addAttribute("page", 0);
            model.addAttribute("competencyRankingProfiles", newProfileDTOFromDB);
        }
        return "redirect:/new-profile/detail";
    }

    /**
     * edit Profile
     *
     * @param model
     * @return
     */
    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int rankingProfileId, Model model) {
        ProfileDTO profileDTO = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesService.findById(rankingProfileId));
        model.addAttribute("page", 0);
        model.addAttribute("competencyRankingProfiles", profileDTO);
        return "redirect:/new-profile/detail";
    }

    /**
     * Get List Detail Or Summary
     *
     * @param userAccountsLoginDTO
     * @param model
     * @return
     */
    @GetMapping(value = {"detail", "detail/{id}"})
    public String detailsEdit(@RequestParam(value = "page", required = false) Integer page,
                              @PathVariable(value = "id", required = false) Integer id,
                              @ModelAttribute("competencyRankingProfiles") ProfileDTO profileDTO,
                              @ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO, Model model) {
        if (page == null) {
            page = 0;
        }
        if (id == null) {
            if (profileDTO.getRankingProfileId() != 0) {
                profileDTO = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesService.findById(profileDTO.getRankingProfileId()));
            } else {
                profileDTO = competencyRankingProfilesService.findFirstByEmployees_EmployeeIdOrderByModifiedDesc(userAccountsLoginDTO.getEmployees().getEmployeeId());
            }
            ProfileDetailWrapperList profileDetailWrapperList = new ProfileDetailWrapperList(competencyRankingProfileDetailsService.findAllByCompetencyRankingPatternDetails_CompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingProfiles_RankingProfileId(profileDTO.getCompetencyRankingPatterns().getCompetencyComponentsList().get(page).getComponentId(), profileDTO.getRankingProfileId()));
            model.addAttribute("competencyRankingProfiles", profileDTO);
            model.addAttribute("profileDetailWrapperList", profileDetailWrapperList);
            model.addAttribute("lastModifiedProfile", competencyRankingProfilesService.findFirstByStatusTypesOrderByModifiedDesc(StatusTypes.Approved));
            model.addAttribute("page", page);
        } else {
            ProfileDTO profileDTO1 = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesService.findById(id));
            model.addAttribute("competencyRankingProfiles", profileDTO1);
            model.addAttribute("profileSummaryList", competencyRankingProfilesService.calculateTotalWeightForProfileSummary(userAccountsLoginDTO.getEmployees(), profileDTO1.getCompetencyRankingProfileDetailsList()));
            model.addAttribute("lastModifiedProfile", competencyRankingProfilesService.findFirstByStatusTypesOrderByModifiedDesc(StatusTypes.Approved));
            model.addAttribute("id", id);
        }
        return "/newprofile/index";
    }

    /**
     * Update Profile
     *
     * @param page
     * @return
     */
    @PostMapping(value = "save/{page}")
    @ResponseBody
    public void updateProfile(@PathVariable(value = "page") Integer page,
                              @ModelAttribute("competencyRankingProfiles") ProfileDTO profileDTO,
                              @RequestBody ProfileDetailWrapperList profileDetailWrapperList) {
        List<CompetencyRankingProfileDetails> profileDetailsList = profileDetailWrapperList.getCompetencyRankingProfileDetailsList();
        profileDTO = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesService.findById(profileDTO.getRankingProfileId()));
        List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList = competencyRankingProfileDetailsService.findAllByCompetencyRankingPatternDetails_CompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingProfiles_RankingProfileId(profileDTO.getCompetencyRankingPatterns().getCompetencyComponentsList().get(page).getComponentId(), profileDTO.getRankingProfileId());
//        Save Detail
        for (int i = 0; i < competencyRankingProfileDetailsList.size(); ++i) {
            DataSources dataSources;
            ProficiencyLevels proficiencyLevels;
            if (profileDetailsList.get(i).getDataSources().getDataSourceId() != 0) {
                dataSources = dataSourceService.findById(profileDetailsList.get(i).getDataSources().getDataSourceId());
            } else {
                dataSources = null;
            }
            if (profileDetailsList.get(i).getProficiencyLevels().getProficiencyLevelId() != 0) {
                proficiencyLevels = proficiencyLevelsService.findProficiencyLevelsById(profileDetailsList.get(i).getProficiencyLevels().getProficiencyLevelId());
            } else {
                proficiencyLevels = null;
            }
            if (dataSources != null) {
                competencyRankingProfileDetailsList.get(i).setDataSources(dataSources);
            }
            if (proficiencyLevels != null) {
                competencyRankingProfileDetailsList.get(i).setProficiencyLevels(proficiencyLevels);
            }
            competencyRankingProfileDetailsService.save(competencyRankingProfileDetailsList.get(i));
        }
        profileDTO.setSelfRakingPoints(competencyRankingProfilesService.calculateSelfCRProfilePoint(ProfileMapper.INSTANCE.fromDTO(profileDTO)));
//        Check Status
        for (CompetencyRankingProfileDetails details : profileDTO.getCompetencyRankingProfileDetailsList()) {
            if (details.getProficiencyLevels() == null) {
                profileDTO.setStatusTypes(StatusTypes.InProcess);
                break;
            } else {
                profileDTO.setStatusTypes(StatusTypes.Completed);
            }
        }
        competencyRankingProfilesService.save(profileDTO);
    }

    /**
     * JSON get list Detail
     *
     * @param page
     * @param profileDTO
     * @return
     */
    @GetMapping("detail-json")
    @ResponseBody
    public ResponseEntity<ProfileDetailWrapperList> detailList(@RequestParam("page") int page,
                                                               @ModelAttribute("competencyRankingProfiles") ProfileDTO profileDTO) {
        ProfileDetailWrapperList profileDetailWrapperList = new ProfileDetailWrapperList(competencyRankingProfileDetailsService.findAllByCompetencyRankingPatternDetails_CompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingProfiles_RankingProfileId(profileDTO.getCompetencyRankingPatterns().getCompetencyComponentsList().get(page).getComponentId(), profileDTO.getRankingProfileId()));

        return new ResponseEntity<>(profileDetailWrapperList, HttpStatus.OK);
    }

    /**
     * JSON get info to show in summary
     *
     * @param
     * @return
     */
    @GetMapping("json-summary")
    @ResponseBody
    public ResponseEntity<List<ProfileSummaryDTO>> getSummaryDTO(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO,
                                                                 @ModelAttribute("competencyRankingProfiles") ProfileDTO profileDTO,
                                                                 @PathVariable(value = "rankingProfileId", required = false) Integer rankingProfileId) {
        if (rankingProfileId != null) {
            profileDTO = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesService.findById(rankingProfileId));
        } else {
            profileDTO = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesService.findById(profileDTO.getRankingProfileId()));
        }
        List<ProfileSummaryDTO> profileSummaryDTOList = competencyRankingProfilesService.calculateTotalWeightForProfileSummary(userAccountsLoginDTO.getEmployees(), profileDTO.getCompetencyRankingProfileDetailsList());
        return new ResponseEntity<>(profileSummaryDTOList, HttpStatus.OK);
    }

    @ModelAttribute
    public UserAccountsLoginDTO user(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        return userAccountsLoginDTO;
    }

    @ModelAttribute("competencyRankingProfiles")
    public ProfileDTO profile(@ModelAttribute("competencyRankingProfiles") ProfileDTO profileDTO) {
        return profileDTO;
    }
}