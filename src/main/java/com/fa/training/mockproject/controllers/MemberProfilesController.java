package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.entities.CompetencyComponents;
import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.CompetencyResults;
import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.entities.dto.*;
import com.fa.training.mockproject.enumeric.StatusTypes;
import com.fa.training.mockproject.services.UserAccountsService;
import com.fa.training.mockproject.services.impl.CompetencyRankingProfilesServiceImpl;
import com.fa.training.mockproject.services.impl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

/**
 * Author Son Nguyen
 */
@Controller
@RequestMapping("/member-profiles")
@SessionAttributes(value = {"user", "profileReview", "filterMemberProfilesCondition", "currentPage"})
public class MemberProfilesController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private CompetencyRankingProfilesServiceImpl competencyRankingProfilesService;

    @Autowired
    private UserAccountsService userAccountsService;

    @ModelAttribute("user")
    public UserAccountsLoginDTO user(Principal principal) {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(principal.getName());
        return userAccountsLoginDTO;
    }

    @GetMapping
    public String home(Model model, HttpSession httpsession, @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO,
                       @SessionAttribute(value = "filterMemberProfilesCondition", required = false) ConditionOfFilterMemberProfilesDTO conditionOfFilterMemberProfilesDTO,
                       @SessionAttribute(value = "currentPage", required = false) Integer currentPage) {
        httpsession.removeAttribute("profileReview");
        model.addAttribute("profileReview", null);
        if (currentPage == null) {
            currentPage = 1;
            model.addAttribute("currentPage", currentPage);
        }
        if (conditionOfFilterMemberProfilesDTO == null) {
            conditionOfFilterMemberProfilesDTO =
                    new ConditionOfFilterMemberProfilesDTO(null, null, "", "", "", "");
            model.addAttribute("filterMemberProfilesCondition", conditionOfFilterMemberProfilesDTO);
        }
        MembersProfilesDTO resultMembersProfilesDTO = competencyRankingProfilesService.getMembersProfiles(conditionOfFilterMemberProfilesDTO, userAccountsLoginDTO, currentPage);
        model.addAttribute("membersProfilesDTO", resultMembersProfilesDTO);
        return "/memberprofiles/MemberProfiles";
    }

    @GetMapping("/return")
    public String returnClearSession(Model model, HttpSession httpSession) {
        httpSession.removeAttribute("profileReview");
        httpSession.removeAttribute("filterMemberProfilesCondition");
        httpSession.removeAttribute("currentPage");
        model.addAttribute("profileReview", null);
        model.addAttribute("filterMemberProfilesCondition", null);
        model.addAttribute("currentPage", null);
        return "redirect:/my-account";
    }

    @GetMapping("/{page}")
    public ResponseEntity<ResultOfFilterMemberProfileDTO> membersProfilePage(@SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO,
                                                                             @SessionAttribute("filterMemberProfilesCondition") ConditionOfFilterMemberProfilesDTO filterMemberProfilesConditionDTO,
                                                                             @PathVariable("page") int page, Model model) {
        model.addAttribute("currentPage", page);
        ResultOfFilterMemberProfileDTO filterMemberProfileResultDTO =
                competencyRankingProfilesService.getMemberProfileByFilter(filterMemberProfilesConditionDTO, userAccountsLoginDTO, page);
        return new ResponseEntity<>(filterMemberProfileResultDTO, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/rankingProfile/{rankingProfileId}/{indexCompetencyComponent}")
    public ResponseEntity<ReviewPageInfoDTO> showEmployeeRankingProfileDetail(@PathVariable("rankingProfileId") int rankingProfileId,
                                                                              @PathVariable("indexCompetencyComponent") int indexCompetencyComponent,
                                                                              @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO, Model model) {
        // Check if profile already Approve => return Error for the access
        CompetencyRankingProfiles competencyRankingProfiles = this.getCompetencyRankingProfile(model, rankingProfileId);
        StatusTypes statusTypesOfProfile = competencyRankingProfiles.getStatusTypes();
        if ((statusTypesOfProfile != StatusTypes.Submitted) && (statusTypesOfProfile != StatusTypes.Reviewing) &&
                (statusTypesOfProfile != StatusTypes.ReviewedFail) && (statusTypesOfProfile != StatusTypes.Reviewed)) {
            return new ResponseEntity<>(new ReviewPageInfoDTO(), HttpStatus.FORBIDDEN);
        }
        // Else return data for review page
        ReviewPageInfoDTO reviewPageInfoDTO = this.getReviewPageResult(userAccountsLoginDTO, rankingProfileId, indexCompetencyComponent, model);
        return new ResponseEntity<>(reviewPageInfoDTO, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/rankingProfile/review")
    public ResponseEntity<Boolean> reviewEmployeeRankingProfile(@RequestBody ReviewLevelDTO reviewLevelDTO, Model model,
                                                                @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        Employees reviewer = userAccountsLoginDTO.getEmployees();
        CompetencyRankingProfiles competencyRankingProfilesToReview = this.getCompetencyRankingProfile(model, reviewLevelDTO.getIdProfile());
        competencyRankingProfilesService.checkAndSetStatusForProfileToReview(competencyRankingProfilesToReview);
        competencyRankingProfilesService.reviewCRProfileDetail(reviewer, competencyRankingProfilesToReview, reviewLevelDTO.getSttOfCompetencyComponent(), reviewLevelDTO.getLevelReviewList());
        boolean canNext = true;
        if ((competencyRankingProfilesToReview.getCompetencyRankingPatterns().getCompetencyComponentsList().size()) == (reviewLevelDTO.getSttOfCompetencyComponent() + 1)) {
            canNext = false;
        }
        return new ResponseEntity<>(canNext, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/rankingProfile/profileSummaryAjax/{rankingProfileId}")
    public ResponseEntity<SummaryPageInforDTO> summaryEmployeeRankingProfile(@PathVariable("rankingProfileId") int rankingProfileId,
                                                                             @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO, Model model) {
        SummaryPageInforDTO summaryPageInforDTO = this.getSummaryPageResult(userAccountsLoginDTO, rankingProfileId, model);
        return new ResponseEntity<>(summaryPageInforDTO, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/filterProfile")
    public ResponseEntity<ResultOfFilterMemberProfileDTO> filterMemberProfile(@RequestBody ConditionOfFilterMemberProfilesDTO filterMemberProfilesConditionDTO,
                                                                              @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO, Model model) {
        model.addAttribute("filterMemberProfilesCondition", filterMemberProfilesConditionDTO);
        ResultOfFilterMemberProfileDTO filterMemberProfileResultDTO =
                competencyRankingProfilesService.getMemberProfileByFilter(filterMemberProfilesConditionDTO, userAccountsLoginDTO, 1);
        return new ResponseEntity<>(filterMemberProfileResultDTO, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/approveSubmittedProfile/{idProfile}")
    public ResponseEntity<String> approveSubmittedProfile(@PathVariable(value = "idProfile") int idProfile, @RequestParam("cmtApprove") String cmtApprove,
                                                          @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO, Model model) {
        CompetencyRankingProfiles competencyRankingProfilesToApprove = this.getCompetencyRankingProfile(model, idProfile);
        return competencyRankingProfilesService.approveProfileByRole(userAccountsLoginDTO, competencyRankingProfilesToApprove, cmtApprove);
    }

    @ResponseBody
    @GetMapping("/rejectSubmittedProfile/{idProfile}")
    public ResponseEntity<String> rejectSubmittedProfile(@PathVariable(value = "idProfile") int idProfile, Model model, @RequestParam("cmtReject") String cmtReject,
                                                         @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        CompetencyRankingProfiles competencyRankingProfilesToReject = this.getCompetencyRankingProfile(model, idProfile);
        return competencyRankingProfilesService.rejectProfileByRole(userAccountsLoginDTO, competencyRankingProfilesToReject, cmtReject);
    }

    @ResponseBody
    @GetMapping("/printProfileAjax/{idProfile}")
    public ResponseEntity<CRProfileReviewAndResultDTO> printProfile(@PathVariable(value = "idProfile") int idProfile,
                                                                    @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO, Model model) {
        Employees reviewer = userAccountsLoginDTO.getEmployees();
        CompetencyRankingProfiles competencyRankingProfilesToPrint = this.getCompetencyRankingProfile(model, idProfile);
        List<CRProfileDetailWithComponentDTO> crProfileDetailWithComponents =
                competencyRankingProfilesService.getCComponentWithPDetailWithReviewResultOfReviewer(competencyRankingProfilesToPrint, reviewer);
        CompetencyResults competencyResultsOfPrintProfile = competencyRankingProfilesService.getCompetencyResultsByReviewerAndProfile(reviewer, competencyRankingProfilesToPrint);
        CRProfileReviewAndResultDTO crProfileReviewAndResultDTO = new CRProfileReviewAndResultDTO();
        crProfileReviewAndResultDTO.setCrProfileDetailWithComponentList(crProfileDetailWithComponents);
        crProfileReviewAndResultDTO.setCompetencyResults(competencyResultsOfPrintProfile);
        return new ResponseEntity<>(crProfileReviewAndResultDTO, HttpStatus.OK);
    }

    /**
     * Author Khiem
     *
     * @param userId
     * @return
     */
    @GetMapping("/getStatus/{idEmployee}")
    public String statusMemberProfile(@PathVariable(value = "idEmployee") Integer userId) {
        employeeService.statusMemberProfile(userId);
        return "redirect:/member-profiles";
    }

    public ReviewPageInfoDTO getReviewPageResult(UserAccountsLoginDTO userAccountsLoginDTO, int rankingProfileId, int indexCompetencyComponent, Model model) {
        Employees reviewer = userAccountsLoginDTO.getEmployees();
        CompetencyRankingProfiles cRProfiles = this.getCompetencyRankingProfile(model, rankingProfileId);
        List<CompetencyComponents> competencyComponentsList = cRProfiles.getCompetencyRankingPatterns().getCompetencyComponentsList();
        CompetencyComponents competencyComponents = competencyComponentsList.get(indexCompetencyComponent);
        ReviewPageInfoDTO reviewPageInfoDTO = competencyRankingProfilesService.findListCRProfileDetailsByIdCComponent(reviewer, competencyComponents.getComponentId(), cRProfiles);
        CompetencyResults competencyResults = competencyRankingProfilesService.getCompetencyResultsByReviewerAndProfile(reviewer, cRProfiles);
        reviewPageInfoDTO.setCompetencyResults(competencyResults);
        reviewPageInfoDTO.setListIndexUnreviewedCComponent(competencyRankingProfilesService.getListIndexUnreviewedCComponent(cRProfiles));
        return reviewPageInfoDTO;
    }

    public SummaryPageInforDTO getSummaryPageResult(UserAccountsLoginDTO userAccountsLoginDTO, int rankingProfileId, Model model) {
        Employees reviewer = userAccountsLoginDTO.getEmployees();
        CompetencyRankingProfiles cRProfiles = this.getCompetencyRankingProfile(model, rankingProfileId);
        List<ProfileSummaryDTO> profileSummaryDTOList = competencyRankingProfilesService.calculateTotalWeightForProfileSummary(reviewer, cRProfiles.getCompetencyRankingProfileDetailsList());
        CompetencyResults competencyResults = competencyRankingProfilesService.getCompetencyResultsByReviewerAndProfile(reviewer, cRProfiles);
        SummaryPageInforDTO summaryPageInforDTO = new SummaryPageInforDTO();
        summaryPageInforDTO.setProfileSummaryDTOList(profileSummaryDTOList);
        summaryPageInforDTO.setCompetencyResults(competencyResults);
        summaryPageInforDTO.setListIndexUnreviewedCComponent(competencyRankingProfilesService.getListIndexUnreviewedCComponent(cRProfiles));
        return summaryPageInforDTO;
    }

    public CompetencyRankingProfiles getCompetencyRankingProfile(Model model, int rankingProfileId) {
        CompetencyRankingProfiles cRProfilesGetFromSession = (CompetencyRankingProfiles) model.getAttribute("profileReview");
        return competencyRankingProfilesService.checkNullAndGetCompetencyRankingProfiles(cRProfilesGetFromSession, rankingProfileId);
    }

    @GetMapping("component-details/{profileId}/{componentName}/{accountRole}")
    @ResponseBody
    public ResponseEntity<List<CompetencyRankingProfileDetailsDTO>> getCompetencyRankingProfileDetails(@PathVariable("profileId") Integer profileId,
                                                                                                       @PathVariable("componentName") String componentName,
                                                                                                       @PathVariable("accountRole") String accountRole,
                                                                                                       @SessionAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        List<CompetencyRankingProfileDetailsDTO> competencyRankingProfileDetailsDTOList =
                competencyRankingProfilesService.getCompetencyRankingProfileDetails(componentName, profileId, userAccountsLoginDTO);
        return new ResponseEntity<>(competencyRankingProfileDetailsDTOList, HttpStatus.OK);
    }
}
