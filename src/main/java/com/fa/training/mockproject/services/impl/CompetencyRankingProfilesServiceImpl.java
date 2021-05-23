package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.entities.dto.*;
import com.fa.training.mockproject.enumeric.StatusTypes;
import com.fa.training.mockproject.repositories.CompetencyRankingProfilesRepository;
import com.fa.training.mockproject.services.*;
import com.fa.training.mockproject.services.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CompetencyRankingProfilesServiceImpl implements CompetencyRankingProfilesService {

    @Autowired
    private CompetencyRankingProfilesRepository competencyRankingProfilesRepository;

    @Autowired
    private ProficiencyLevelsServiceImpl proficiencyLevelsService;

    @Autowired
    private CompetencyRankingProfileDetailsServiceImpl competencyRankingProfileDetailsService;

    @Autowired
    private CompetencyReviewRankingsServiceImpl competencyReviewRankingsService;

    @Autowired
    private JobRolesServiceImpl jobRolesService;

    @Autowired
    private CompetencyResultsServiceImpl competencyResultsService;

    @Autowired
    private UserAccountsService userAccountsService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PeriodPatternService periodPatternService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private CompetencyComponentsService competencyComponentsService;

    @Override
    public void save(CompetencyRankingProfiles competencyRankingProfiles) {
        competencyRankingProfilesRepository.save(competencyRankingProfiles);
    }

    @Override
    public boolean existsByProfileTitleAndCompetencyRankingPatterns_PeriodPattern_PeriodIdAndCompetencyRankingPatterns_JobRoles_JobRoleIdAndCompetencyRankingPatterns_JobRoles_Domains_DomainId(String title, long periodId, int jobrolesId, int domainId) {
        if (competencyRankingProfilesRepository.existsByProfileTitleAndCompetencyRankingPatterns_PeriodPattern_PeriodIdAndCompetencyRankingPatterns_JobRoles_JobRoleIdAndCompetencyRankingPatterns_JobRoles_Domains_DomainId(title, periodId, jobrolesId, domainId)) {
            return true;
        } else {
            return false;
        }
    }

    public CompetencyRankingProfiles findById(int id) {
        return competencyRankingProfilesRepository.findByRankingProfileId(id);
    }

    @Override
    public void save(ProfileDTO profileDTO) {
        competencyRankingProfilesRepository.save(ProfileMapper.INSTANCE.fromDTO(profileDTO));
    }

    @Override
    public void addCompetencyRankingProfiles(CompetencyRankingProfiles competencyRankingProfiles, String email) {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(email);
        competencyRankingProfiles.setEmployees(userAccountsLoginDTO.getEmployees());
        competencyRankingProfilesRepository.save(competencyRankingProfiles);
    }

    @Override
    public ProfileDTO findFirstByEmployees_EmployeeIdOrderByModifiedDesc(int employeeId) {
        return ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesRepository.findFirstByEmployees_EmployeeIdOrderByModifiedDesc(employeeId));
    }

    @Override
    public ProfileDTO findFirstByStatusTypesOrderByModifiedDesc(StatusTypes statusTypes) {
        ProfileDTO profileDTO = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesRepository.findFirstByStatusTypesOrderByModifiedDesc(statusTypes));
        return profileDTO;
    }

    @Override
    public void deleteById(int id) {
        competencyRankingProfilesRepository.deleteById(id);
    }

    public Page<CompetencyRankingProfiles> findListSubmittedCompetencyRankingProfilesByEmployeeId(int idEmployee, int page) {
        return competencyRankingProfilesRepository.findByEmployees_EmployeeIdAndStatusTypesOrStatusTypesOrStatusTypesOrStatusTypesOrderByCreatedDesc
                (idEmployee, StatusTypes.Submitted, StatusTypes.Reviewing, StatusTypes.Reviewed, StatusTypes.ReviewedFail, PageRequest.of(page - 1, 5));
    }

    public Page<CompetencyRankingProfiles> findListReviewedCompetencyRankingProfilesByEmployeeId(int idEmployee, int page) {
        return competencyRankingProfilesRepository.findByEmployees_EmployeeIdAndStatusTypesOrStatusTypesOrStatusTypesOrStatusTypesOrderByCreatedDesc
                (idEmployee, StatusTypes.Reviewed, StatusTypes.Reviewed, StatusTypes.Approved, StatusTypes.Rejected, PageRequest.of(page - 1, 5));
    }

    public List<CompetencyRankingProfiles> findAllProfile() {
        return competencyRankingProfilesRepository.findAll();
    }

    public Page<CompetencyRankingProfiles> findAllSubmittedProfileBySearcherPaging(UserAccountsLoginDTO searcher, int page) {
        Page<CompetencyRankingProfiles> competencyRankingProfilesPage =
                competencyRankingProfilesRepository.findByCompetencyRankingPatterns_JobRoles_Domains_DomainNameAndStatusTypesOrStatusTypesOrStatusTypesOrStatusTypesOrderBySubmittedDateDesc
                        (searcher.getEmployees().getDomains().getDomainName(), StatusTypes.Submitted, StatusTypes.Reviewing, StatusTypes.Reviewed, StatusTypes.ReviewedFail, PageRequest.of(page, 5));
        return competencyRankingProfilesPage;
    }

    public Page<CompetencyRankingProfiles> findAllReviewedProfile(int page) {
        return competencyRankingProfilesRepository.findByStatusTypesOrStatusTypesOrStatusTypesOrStatusTypesOrderByCompetencyResults_ReviewedDateDesc
                (StatusTypes.Reviewed, StatusTypes.Reviewed, StatusTypes.Approved, StatusTypes.Rejected, PageRequest.of(page, 5));
    }

    @Override
    public Page<ProfileDTO> findAllByNoFilter(int empId, int page) {
        Page<CompetencyRankingProfiles> competencyRankingProfilesPage = competencyRankingProfilesRepository.findAllByEmployeesOrderByCreatedDesc(employeeService.findById(empId), PageRequest.of(page, 5));
        Page<ProfileDTO> profileDTOPage = competencyRankingProfilesPage.map(this::convertDTO);
        return profileDTOPage;
    }

    @Override
    public Page<ProfileDTO> findAllByFilter(String title, Long periodId, Date created, String status, Integer rolesId, int empId, int page) {
        Page<CompetencyRankingProfiles> competencyRankingProfilesPage;
        if (title == null && periodId == null && created == null && status == null && rolesId == null) {
            competencyRankingProfilesPage = competencyRankingProfilesRepository.findAllByEmployeesOrderByCreatedDesc(employeeService.findById(empId), PageRequest.of(page, 5));
        } else {
            competencyRankingProfilesPage = competencyRankingProfilesRepository.findAllByFilter(title, periodId, created, status, rolesId, employeeService.findById(empId), PageRequest.of(page, 5));
        }
        Page<ProfileDTO> profileDTOPage = competencyRankingProfilesPage.map(this::convertDTO);
        return profileDTOPage;
    }

    /**
     * Function to get Employee Profiles DTO by given employeeId
     * Author Son Nguyen
     *
     * @param employeeId
     * @return
     */
    public EmployeeProfilesDTO getEmployeeProfilesDTOToReview(int employeeId, int page) {
        EmployeeProfilesDTO employeeProfilesDTO = new EmployeeProfilesDTO();
        employeeProfilesDTO.setEmployees(employeeService.findById(employeeId));
        Page<CompetencyRankingProfiles> competencyRankingProfilesPage = this.findListSubmittedCompetencyRankingProfilesByEmployeeId(employeeId, page);
        int totalPage = competencyRankingProfilesPage.getTotalPages();
        ResultOfFilterMemberProfileDTO filterMemberProfileResultDTO = this.getListPage(totalPage, page);
        employeeProfilesDTO.setListPage(filterMemberProfileResultDTO.getListPage());
        employeeProfilesDTO.setCurrentPage(page);
        employeeProfilesDTO.setCompetencyRankingProfilesList(competencyRankingProfilesPage.getContent());
        return employeeProfilesDTO;
    }

    /**
     * Function to get FilterMemberProfileResultDTO
     *
     * @param filterMemberProfilesConditionDTO
     * @param searcher
     * @param page
     * @return
     */
    public ResultOfFilterMemberProfileDTO getMemberProfileByFilter(ConditionOfFilterMemberProfilesDTO filterMemberProfilesConditionDTO, UserAccountsLoginDTO searcher, int page) {
        Page<CompetencyRankingProfiles> allSubmittedProfileBySearcherPaging = null;
        // Get Member Name to search
        String memberNameFilter = null;
        if (!filterMemberProfilesConditionDTO.getMemberName().equals("")) {
            memberNameFilter = filterMemberProfilesConditionDTO.getMemberName();
        }
        // Get Date and a day before date to search
        Date dateSearch1 = null;
        Date dateSearch2 = null;
        if (!(filterMemberProfilesConditionDTO.getSubmittedDate().equals(""))) {
            try {
                dateSearch1 = new SimpleDateFormat("yyyy-MM-dd").parse(filterMemberProfilesConditionDTO.getSubmittedDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateSearch2 = new Date(dateSearch1.getTime() + 86400000);
        }
        // Get Domain, Status, date reviewed or submitted of profile condition to search base on role
        String domain = null;
        StatusTypes statusTypes1 = null;
        StatusTypes statusTypes2 = null;
        StatusTypes statusTypes3 = null;
        StatusTypes statusTypes4 = null;
        StatusTypes statusTypesFilter = null;
        Boolean filterByStatus = false;
        // Check does filterMemberProfilesConditionDTO have status value
        // If yes get status to filter
        // If not => set status base on role.
        if (!(filterMemberProfilesConditionDTO.getStatus().equals(""))) {
            filterByStatus = true;
            for (StatusTypes statusType : StatusTypes.values()) {
                if ((statusType.name()).equals(filterMemberProfilesConditionDTO.getStatus())) {
                    statusTypesFilter = statusType;
                    break;
                }
            }
        }
        if (searcher.getUserRoles().getUserRoleName().equals("ROLE_LEADER")) {
            domain = searcher.getEmployees().getDomains().getDomainName();
            if (!filterByStatus) {
                statusTypes1 = StatusTypes.Submitted;
                statusTypes2 = StatusTypes.Reviewing;
                statusTypes3 = StatusTypes.Reviewed;
                statusTypes4 = StatusTypes.ReviewedFail;
            }
            allSubmittedProfileBySearcherPaging = competencyRankingProfilesRepository.leaderFindMemberProfileByFilter(
                    filterMemberProfilesConditionDTO.getJobRoleId(),
                    filterMemberProfilesConditionDTO.getPeriodId(),
                    memberNameFilter,
                    dateSearch1,
                    dateSearch2,
                    domain,
                    statusTypes1, statusTypes2, statusTypes3, statusTypes4,
                    statusTypesFilter,
                    PageRequest.of(page - 1, 5));
        } else if (searcher.getUserRoles().getUserRoleName().equals("ROLE_MANAGER")) {
            if (!filterMemberProfilesConditionDTO.getDomainName().equals("")) {
                domain = filterMemberProfilesConditionDTO.getDomainName();
            }
            if (!filterByStatus) {
                statusTypes1 = StatusTypes.Reviewed;
                statusTypes2 = StatusTypes.Approved;
                statusTypes3 = StatusTypes.Rejected;
                statusTypes4 = StatusTypes.Rejected;
            }
            allSubmittedProfileBySearcherPaging = competencyRankingProfilesRepository.managerFindMemberProfileByFilter(
                    filterMemberProfilesConditionDTO.getJobRoleId(),
                    filterMemberProfilesConditionDTO.getPeriodId(),
                    memberNameFilter,
                    dateSearch1,
                    dateSearch2,
                    domain,
                    statusTypes1, statusTypes2, statusTypes3, statusTypes4,
                    statusTypesFilter,
                    PageRequest.of(page - 1, 5));
        }
        int totalPage = allSubmittedProfileBySearcherPaging.getTotalPages();
        ResultOfFilterMemberProfileDTO filterMemberProfileResultDTO = this.getListPage(totalPage, page);
        filterMemberProfileResultDTO.setCompetencyRankingProfilesFilterList(allSubmittedProfileBySearcherPaging.getContent());
        return filterMemberProfileResultDTO;
    }

    public ResultOfFilterMemberProfileDTO getListPage(int totalPage, int currentPage) {
        ResultOfFilterMemberProfileDTO filterMemberProfileResultDTO = new ResultOfFilterMemberProfileDTO();
        List<Integer> listPage = new ArrayList<>();
        if (currentPage == 1) {
            listPage.add(currentPage);
            if ((currentPage + 1) <= totalPage) {
                listPage.add(currentPage + 1);
            }
            if ((currentPage + 2) <= totalPage) {
                listPage.add(currentPage + 2);
            }
        } else if (currentPage == totalPage) {
            if ((totalPage - 2) >= 1) {
                listPage.add(totalPage - 2);
            }
            if ((totalPage - 1) >= 1) {
                listPage.add(totalPage - 1);
            }
            listPage.add(totalPage);
        } else {
            listPage.add(currentPage - 1);
            listPage.add(currentPage);
            listPage.add(currentPage + 1);
        }
        filterMemberProfileResultDTO.setListPage(listPage);
        filterMemberProfileResultDTO.setCurrentPage(currentPage);
        return filterMemberProfileResultDTO;
    }

    /**
     * function is used to check does competency null or not if yes function will find the profile by given rankingProfileId.
     * Author Son Nguyen
     *
     * @param competencyRankingProfiles
     * @param rankingProfileId
     * @return
     */
    public CompetencyRankingProfiles checkNullAndGetCompetencyRankingProfiles(CompetencyRankingProfiles competencyRankingProfiles, int rankingProfileId) {
        try {
            competencyRankingProfiles.getRankingProfileId();
            competencyRankingProfiles.getCompetencyRankingPatterns().getCompetencyComponentsList();
            return competencyRankingProfiles;
        } catch (NullPointerException e) {
            competencyRankingProfiles = findById(rankingProfileId);
            competencyRankingProfiles.getCompetencyRankingPatterns().getCompetencyComponentsList();
            return competencyRankingProfiles;
        }
    }

    /**
     * function is used to find list competency ranking profile by given competency component.
     * Author Son Nguyen
     *
     * @param competencyComponentId
     * @param cRProfiles
     * @return
     */
    public ReviewPageInfoDTO findListCRProfileDetailsByIdCComponent(Employees reviewer, int competencyComponentId, CompetencyRankingProfiles cRProfiles) {
        // Get competency component in component list of ranking pattern to review
        CompetencyComponents competencyComponentsReview = this.getCComponentByIdCComponent(cRProfiles.getCompetencyRankingPatterns().getCompetencyComponentsList(), competencyComponentId);
        ReviewPageInfoDTO profileDetailListAndTotalWeight = new ReviewPageInfoDTO();
        List<CRProfileDetailAndReviewLevelDTO> cRProfileDetailsList = new ArrayList<>();
        int totalWeight = 0;
        // Loop each component detail and loop each pattern detail if id of component detail equal with id of pattern detail => take the pattern detail
        // then call function to find the CompetencyReviewRankings of this pattern detail. Set that 2 values into CRProfileDetailAndReviewLevelDTO
        // then add them into cRProfileDetailsList
        // finally return the object with 2 value: List Object cRProfileDetailsList(2 value: profileDetails, CompetencyReviewRanking), totalWeight.
        for (CompetencyComponentDetails componentDetails : competencyComponentsReview.getCompetencyComponentDetailsList()) {
            for (CompetencyRankingProfileDetails profileDetails : cRProfiles.getCompetencyRankingProfileDetailsList()) {
                if (profileDetails.getCompetencyRankingPatternDetails().getCompetencyComponentDetails().getComponentDetailId() == componentDetails.getComponentDetailId()) {
                    CRProfileDetailAndReviewLevelDTO crProfileDetailAndReviewLevelDTO = new CRProfileDetailAndReviewLevelDTO();
                    CompetencyReviewRankings competencyReviewRankingOfThisCRProfileDetail = this.getCompetencyReviewRanking(reviewer, profileDetails);
                    crProfileDetailAndReviewLevelDTO.setCompetencyRankingProfileDetails(profileDetails);
                    crProfileDetailAndReviewLevelDTO.setCompetencyReviewRankings(competencyReviewRankingOfThisCRProfileDetail);
                    cRProfileDetailsList.add(crProfileDetailAndReviewLevelDTO);
                    totalWeight = totalWeight + profileDetails.getCompetencyRankingPatternDetails().getRankingWeight();
                    break;
                }
            }
        }
        profileDetailListAndTotalWeight.setCrProfileDetailAndReviewLevelList(cRProfileDetailsList);
        profileDetailListAndTotalWeight.setTotalWeight(totalWeight);
        return profileDetailListAndTotalWeight;
    }

    public CompetencyComponents getCComponentByIdCComponent(List<CompetencyComponents> competencyComponentsList, int idCompetencyComponents) {
        for (CompetencyComponents competencyComponents : competencyComponentsList) {
            if (competencyComponents.getComponentId() == idCompetencyComponents) {
                return competencyComponents;
            }
        }
        return new CompetencyComponents();
    }

    /**
     * Function is used to calculate self ranking point of the input CompetencyRankingProfiles after employee create new profile.
     * Author Son Nguyen
     * To Show in review profile of Leader Role.
     *
     * @param competencyRankingProfiles
     * @return
     */
    public String calculateSelfCRProfilePoint(CompetencyRankingProfiles competencyRankingProfiles) {
        float totalPoint = 0;
        float levelOfSelfRanked, maxLevel, weightOfPatternDetail, weightOfSelfRanked;
        List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList = competencyRankingProfiles.getCompetencyRankingProfileDetailsList();
        for (CompetencyRankingProfileDetails competencyRankingProfileDetail : competencyRankingProfileDetailsList) {
            try {
                levelOfSelfRanked = competencyRankingProfileDetail.getProficiencyLevels().getProficiencyLevel();
            } catch (NullPointerException ex) {
                levelOfSelfRanked = 0;
            }
            try {
                maxLevel = proficiencyLevelsService.findFirstByProficiencyLevelTypes_ProficiencyLevelTypeIdAndDesc(competencyRankingProfileDetail.getCompetencyRankingPatternDetails().getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevelTypes().getProficiencyLevelTypeId()).getProficiencyLevel();
            } catch (NullPointerException e) {
                maxLevel = 0;
            }
            weightOfPatternDetail = competencyRankingProfileDetail.getCompetencyRankingPatternDetails().getRankingWeight();
            weightOfSelfRanked = levelOfSelfRanked * weightOfPatternDetail / maxLevel;
            totalPoint = totalPoint + weightOfSelfRanked;
        }
        return this.formatNicelyFloat(totalPoint);
    }

    /**
     * function is used to save the review level of each ranking profile detail by given profile to review, competency component id,
     * List of level manager already chosen.
     * Author Son Nguyen
     *
     * @param cRProfileReview
     * @param sttOfCompetencyComponent
     * @param proficiencyLevelList
     * @return
     */
    public CompetencyResults reviewCRProfileDetail(Employees reviewer, CompetencyRankingProfiles cRProfileReview, int sttOfCompetencyComponent, List<Integer> proficiencyLevelList) {
        List<CompetencyComponents> competencyComponentsList = cRProfileReview.getCompetencyRankingPatterns().getCompetencyComponentsList();
        CompetencyComponents competencyComponentsReview = competencyComponentsList.get(sttOfCompetencyComponent);
        // Get profile Detail List of given Competency Ranking Profiles by competency component Id
        List<CRProfileDetailAndReviewLevelDTO> profileDetailList =
                this.findListCRProfileDetailsByIdCComponent(reviewer, competencyComponentsReview.getComponentId(), cRProfileReview).getCrProfileDetailAndReviewLevelList();
        // Loop to set lever review for each profile detail
        for (int i = 0; i < profileDetailList.size(); i++) {
            // Get CompetencyReviewRankings of CompetencyRankingProfileDetail
            CompetencyReviewRankings cReviewRankingsOfProfileDetail = profileDetailList.get(i).getCompetencyReviewRankings();
            // Set proficiency level by value manager already chosen to set for competency review ranking.
            ProficiencyLevels pLevelsReviewed = proficiencyLevelsService.findProficiencyLevelsById(proficiencyLevelList.get(i));
            cReviewRankingsOfProfileDetail.setProficiencyLevels(pLevelsReviewed);
            // After set level reviewed (CompetencyReviewRankings) for profileDetail i will save CompetencyReviewRankings into database
            competencyReviewRankingsService.save(cReviewRankingsOfProfileDetail);
            competencyRankingProfileDetailsService.save(profileDetailList.get(i).getCompetencyRankingProfileDetails());
        }
        // Calculate and save CompetencyResults for review profile.
        CompetencyResults competencyResultsOfReviewProfile = this.calculateReviewCRProfilePoint(reviewer, cRProfileReview);
        competencyResultsService.save(competencyResultsOfReviewProfile);
        return competencyResultsOfReviewProfile;
    }

    /**
     * Function is used to check does CompetencyRankingProfileDetails have CompetencyReviewRankings with the reviewer input or not
     * If Yes function will find and return the CompetencyReviewRankings
     * If Not function will create new and set CompetencyRankingProfileDetails and Reviewer for the CompetencyReviewRankings the return And Function Also create
     * a list of CompetencyReviewRankings for CompetencyRankingProfileDetails).
     * Author Son Nguyen
     *
     * @param reviewer
     * @param profileDetails
     * @return
     */
    public CompetencyReviewRankings getCompetencyReviewRanking(Employees reviewer, CompetencyRankingProfileDetails profileDetails) {
        // Find CompetencyReviewRankings of CompetencyRankingProfileDetails
        if (profileDetails.getCompetencyRankingProfiles().getStatusTypes() != StatusTypes.Submitted) {
            try {
                profileDetails.getCompetencyReviewRankings().getCompetencyReviewRankingsId();
                return profileDetails.getCompetencyReviewRankings();
            } catch (NullPointerException e) {
            }
        }
        // If profileDetails dont have CompetencyReviewRankings =>> Create New
        // Create New CompetencyReviewRankings for CompetencyRankingProfileDetails
        CompetencyReviewRankings competencyReviewRankings = new CompetencyReviewRankings();
        competencyReviewRankings.setReviewer(reviewer);
        competencyReviewRankings.setCompetencyRankingProfileDetails(profileDetails);
        // Set List CompetencyReviewRankings for CompetencyRankingProfileDetails.
        profileDetails.setCompetencyReviewRankings(competencyReviewRankings);
        competencyReviewRankingsService.save(competencyReviewRankings);
        competencyRankingProfileDetailsService.save(profileDetails);
        return competencyReviewRankings;
    }

    /**
     * Function is used to check and set Status for profile to Show in review profile of Leader Role.
     * Author Son Nguyen
     *
     * @param competencyRankingProfilesToCheck
     */
    public void checkAndSetStatusForProfileToReview(CompetencyRankingProfiles competencyRankingProfilesToCheck) {
        if ((competencyRankingProfilesToCheck.getStatusTypes() == StatusTypes.Submitted)) {
            competencyRankingProfilesToCheck.setStatusTypes(StatusTypes.Reviewing);
        }
    }

    /**
     * Function is use to check does profile can approve, reject or not.
     *
     * @param competencyRankingProfilesToApprove
     * @return
     */
    public boolean checkConditionProfileToReview(CompetencyRankingProfiles competencyRankingProfilesToApprove) {
        // Check does profile already approve or reject. If yes => pro file already reviewed all profile detail => can approve or reject.
        if ((competencyRankingProfilesToApprove.getStatusTypes() == StatusTypes.Reviewed) ||
                (competencyRankingProfilesToApprove.getStatusTypes() == StatusTypes.ReviewedFail)) {
            return true;
        }
        // If profile is in status reviewing => have to check does all profile detail already reviewed yet?
        // if yes => profile can approve or reject -
        // if not => can not approve or reject when have some detail still not reviewed yet.
        if ((competencyRankingProfilesToApprove.getStatusTypes() == StatusTypes.Reviewing)) {
            for (CompetencyRankingProfileDetails competencyRankingProfileDetails : competencyRankingProfilesToApprove.getCompetencyRankingProfileDetailsList()) {
                try {
                    competencyRankingProfileDetails.getCompetencyReviewRankings().getProficiencyLevels().getProficiencyLevelName();
                } catch (NullPointerException e) {
                    return false;
                }
            }
            return true;
        }
        // if status profile not in above status => can not approve or
        return false;
    }

    /**
     * Function is use to check does profile can approve, reject or not.
     *
     * @param competencyRankingProfilesToApprove
     * @return
     */
    public boolean checkConditionProfileToApprove(CompetencyRankingProfiles competencyRankingProfilesToApprove) {
        if ((competencyRankingProfilesToApprove.getStatusTypes() == StatusTypes.Reviewed) ||
                (competencyRankingProfilesToApprove.getStatusTypes() == StatusTypes.Approved) ||
                (competencyRankingProfilesToApprove.getStatusTypes() == StatusTypes.Rejected)) {
            return true;
        }
        return false;
    }

    /**
     * Function is used to calculate profile ranking point after leader Review a competency component.
     * To Show in review profile of Leader Role.
     * Author Son Nguyen
     *
     * @param reviewer
     * @param competencyRankingProfiles
     * @return
     */
    public CompetencyResults calculateReviewCRProfilePoint(Employees reviewer, CompetencyRankingProfiles competencyRankingProfiles) {
        List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsListToCalculate = competencyRankingProfiles.getCompetencyRankingProfileDetailsList();
        float profileReviewRankingPoint = 0;
        for (CompetencyRankingProfileDetails competencyRankingProfileDetailsCalculate : competencyRankingProfileDetailsListToCalculate) {
            try {
                competencyRankingProfileDetailsCalculate.getCompetencyReviewRankings().getCompetencyReviewRankingsId();
            } catch (NullPointerException e) {
                continue;
            }
            profileReviewRankingPoint = profileReviewRankingPoint + calculateSelfRankedAndReviewRankedWeight(reviewer, competencyRankingProfileDetailsCalculate).get(1);
        }
        CompetencyResults competencyResults = this.getCompetencyResultsByReviewerAndProfile(reviewer, competencyRankingProfiles);
        competencyResults.setReviewRakingPoints(profileReviewRankingPoint);
        return competencyResults;
    }

    /**
     * Function is used calculate total weight in a competency component, total self raking weight in a competency component,
     * total review raking weight in a competency component of a profile.
     * Author Son Nguyen
     *
     * @param competencyRankingProfileDetailsList
     * @return
     */
    public List<ProfileSummaryDTO> calculateTotalWeightForProfileSummary(Employees reviewer, List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList) {
        List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsListToFind = new ArrayList<CompetencyRankingProfileDetails>(competencyRankingProfileDetailsList);
        List<ProfileSummaryDTO> profileSummaryDTOList = new ArrayList<>();
        Iterator<CompetencyRankingProfileDetails> iteratorCompetencyRankingProfileDetails = competencyRankingProfileDetailsListToFind.iterator();
        while (iteratorCompetencyRankingProfileDetails.hasNext()) {
            CompetencyRankingProfileDetails competencyRankingProfileDetails = iteratorCompetencyRankingProfileDetails.next();
            // This Object will contain 4 values 1: competency component name, 2: total Weight of component, 3: self ranking weight, 4: review ranking weight.
            ProfileSummaryDTO profileSummaryDTO = new ProfileSummaryDTO();
            // Competency Components to work with.
            CompetencyComponents competencyComponents = competencyRankingProfileDetails.getCompetencyRankingPatternDetails().getCompetencyComponentDetails().getCompetencyComponents();
            // This list will contain the competency ranking profile detail(CRPD) have in the same competency component to remove out of loop List (loop faster).
            List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsListToRemove = new ArrayList<>();
            float totalWeight = 0;
            float selfRankWeight = 0;
            float reviewRankWeight = 0;
            float patternRankWeight = 0;//
            for (CompetencyRankingProfileDetails competencyRankingProfileDetailsCalculate : competencyRankingProfileDetailsListToFind) {
                // The code below will check does CRPD have the same competency component is working?
                // if yes => it will calculate the totalWeight, selfRankWeight, reviewRankWeight of the group of CRPD belong to the competency component.
                // if not => it will break the loop (BC the detail of the loop always at first indexes)
                if (competencyRankingProfileDetailsCalculate.getCompetencyRankingPatternDetails().getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == competencyComponents.getComponentId()) {
                    competencyRankingProfileDetailsListToRemove.add(competencyRankingProfileDetailsCalculate);
                    List<Float> selfRankedAndReviewRankedWeight = calculateSelfRankedAndReviewRankedWeight(reviewer, competencyRankingProfileDetailsCalculate);
                    selfRankWeight += selfRankedAndReviewRankedWeight.get(0);
                    reviewRankWeight += selfRankedAndReviewRankedWeight.get(1);
                    patternRankWeight += selfRankedAndReviewRankedWeight.get(2);
                    // Tan code
                    // Step 1: Get level
                    // Step 2: Get max
                    // Step 3: Calculate
                    int levelOfDetailsComponent = competencyRankingProfileDetailsCalculate.getCompetencyRankingPatternDetails().getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevel();
                    int maxSizeOfTypeLevelForDetailsComponent = competencyRankingProfileDetailsCalculate.getCompetencyRankingPatternDetails().getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevelTypes().getProficiencyLevelsList().size();
                    int maxLevelOfTypeLevelForDetailsComponent = competencyRankingProfileDetailsCalculate.getCompetencyRankingPatternDetails().getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevelTypes().getProficiencyLevelsList().get(maxSizeOfTypeLevelForDetailsComponent - 1).getProficiencyLevel();
                    totalWeight += (levelOfDetailsComponent * 1.0 / maxLevelOfTypeLevelForDetailsComponent) * competencyRankingProfileDetailsCalculate.getCompetencyRankingPatternDetails().getRankingWeight();
                    if (competencyRankingProfileDetailsCalculate.getProficiencyLevels() == null) {
                        profileSummaryDTO.setStatus(0);
                    } else {
                        profileSummaryDTO.setStatus(1);
                    }
                }
                if ((competencyRankingProfileDetailsCalculate.getCompetencyRankingPatternDetails().getCompetencyComponentDetails().
                        getCompetencyComponents().getComponentId() != competencyComponents.getComponentId()) ||
                        (competencyRankingProfileDetailsListToRemove.size() == competencyRankingProfileDetailsListToFind.size())) {
                    profileSummaryDTO.setCompetencyComponents(competencyComponents);
                    profileSummaryDTO.setTotalWeight(formatNicelyFloat(totalWeight));
                    profileSummaryDTO.setSelfRankWeight(formatNicelyFloat(selfRankWeight));
                    profileSummaryDTO.setReviewRankWeight(formatNicelyFloat(reviewRankWeight));
                    profileSummaryDTO.setPatternRankWeight(formatNicelyFloat(patternRankWeight));//
                    profileSummaryDTO.setCompetencyRankingPatterns(competencyRankingProfileDetailsList.get(0).getCompetencyRankingPatternDetails().getCompetencyRankingPatterns());//
                    profileSummaryDTO.setCompetencyRankingProfiles(competencyRankingProfileDetailsList.get(0).getCompetencyRankingProfiles());//
                    break;
                }
            }

            profileSummaryDTOList.add(profileSummaryDTO);
            competencyRankingProfileDetailsListToFind.removeAll(competencyRankingProfileDetailsListToRemove);
            iteratorCompetencyRankingProfileDetails = competencyRankingProfileDetailsListToFind.iterator();
        }
        return profileSummaryDTOList;
    }

    /**
     * Function is used to calculate the Self Ranked Weight and Review Ranked Weight then return the list of 2 that values.
     * Author Son Nguyen
     *
     * @param profileDetails
     * @return
     */
    public List<Float> calculateSelfRankedAndReviewRankedWeight(Employees reviewer, CompetencyRankingProfileDetails profileDetails) {
        List<Float> selfRankAndReviewRankPoints = new ArrayList<>();
        float weight = 0;
        try {
            weight = profileDetails.getCompetencyRankingPatternDetails().getRankingWeight();
        } catch (NullPointerException e) {
            weight = 0;
        }
        float maxLevel = 0;
        try {
            maxLevel = proficiencyLevelsService.findFirstByProficiencyLevelTypes_ProficiencyLevelTypeIdAndDesc(profileDetails.getCompetencyRankingPatternDetails().getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevelTypes().getProficiencyLevelTypeId()).getProficiencyLevel();
        } catch (NullPointerException e) {
            maxLevel = 0;
        }
        float levelSelfRankedOfCRProfileDetail = 0;
        try {
            levelSelfRankedOfCRProfileDetail = profileDetails.getProficiencyLevels().getProficiencyLevel();
        } catch (NullPointerException e) {
            levelSelfRankedOfCRProfileDetail = 0;
        }
        float reviewRankWeight = 0;
        CompetencyReviewRankings competencyReviewRankings = this.getCompetencyReviewRanking(reviewer, profileDetails);
        // This will check does profile detail already reviewed or not
        // If yes it will calculate the reviewRankWeight;
        // If Not the reviewRankWeight will not calculate then = 0 like default;
        if (competencyReviewRankings.getProficiencyLevels() != null) {
            float levelReviewedOfCRProfileDetail = competencyReviewRankings.getProficiencyLevels().getProficiencyLevel();
            reviewRankWeight = levelReviewedOfCRProfileDetail * weight / maxLevel;
        }
        float selfRankWeight = levelSelfRankedOfCRProfileDetail * weight / maxLevel;
        selfRankAndReviewRankPoints.add(selfRankWeight);
        selfRankAndReviewRankPoints.add(reviewRankWeight);
        float patternLevels = 0;
        try {
            patternLevels = profileDetails.getCompetencyRankingPatternDetails().getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevel();
        } catch (NullPointerException e) {
            patternLevels = 0;
        }
        float patternRank = patternLevels / maxLevel * weight;
        selfRankAndReviewRankPoints.add(patternRank);
        return selfRankAndReviewRankPoints;
    }

    /**
     * Function is used to format the float number.
     * Author Son Nguyen
     *
     * @param value
     * @return
     */
    public String formatNicelyFloat(float value) {
        if (value == (long) value)
            return String.format("%d", (long) value);
        else {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(value);
        }
    }

    /**
     * Function is use to get CompetencyResults By Reviewer And Profile given.
     * Author Son Nguyen.
     *
     * @param reviewer
     * @param competencyRankingProfilesToFindCompetencyResult
     * @return
     */
    public CompetencyResults getCompetencyResultsByReviewerAndProfile(Employees reviewer, CompetencyRankingProfiles competencyRankingProfilesToFindCompetencyResult) {
        CompetencyResults competencyResultsOfProfile;
        try {
            competencyRankingProfilesToFindCompetencyResult.getCompetencyResults().getReviewer().getEmployeeId();
            return competencyRankingProfilesToFindCompetencyResult.getCompetencyResults();
        } catch (NullPointerException e) {
            //  If profile just submitted =>  Create new CompetencyResults, then set reviewer, profile, first point = 0 file result;
            competencyResultsOfProfile = new CompetencyResults();
            competencyResultsOfProfile.setReviewer(reviewer);
            competencyResultsOfProfile.setCompetencyRankingProfile(competencyRankingProfilesToFindCompetencyResult);
            competencyResultsOfProfile.setReviewRakingPoints(0);
            competencyRankingProfilesToFindCompetencyResult.setCompetencyResults(competencyResultsOfProfile);
            competencyResultsService.save(competencyResultsOfProfile);
            this.save(competencyRankingProfilesToFindCompetencyResult);
        }
        return competencyResultsOfProfile;
    }

    /**
     * Function is use to get the Competency component with Ranking Profile Detail and Review result of the profile detail to print the profile of Employee.
     * Author Son Nguyen
     *
     * @param competencyRankingProfiles
     * @param reviewer
     * @return
     */
    public List<CRProfileDetailWithComponentDTO> getCComponentWithPDetailWithReviewResultOfReviewer(CompetencyRankingProfiles competencyRankingProfiles, Employees reviewer) {
        List<CRProfileDetailWithComponentDTO> cComponentWithPDetailWithReviewResultOfReviewerDTOsList = new ArrayList<>();
        List<CompetencyComponents> competencyComponentsListOfProfile = competencyRankingProfiles.getCompetencyRankingPatterns().getCompetencyComponentsList();
        // This loop will find and set 3 information for ProfileInforToPrintDTO: 1,2. ProfileDetail With ReviewResult 3. CompetencyComponents.
        for (CompetencyComponents components : competencyComponentsListOfProfile) {
            CRProfileDetailWithComponentDTO cComponentWithPDetailWithReviewResultOfReviewerDTO = new CRProfileDetailWithComponentDTO();
            ReviewPageInfoDTO profileDetailListAndTotalWeight = this.findListCRProfileDetailsByIdCComponent(reviewer, components.getComponentId(), competencyRankingProfiles);
            List<CRProfileDetailAndReviewLevelDTO> profileDetailWithReviewResultDTOList = profileDetailListAndTotalWeight.getCrProfileDetailAndReviewLevelList();
            // Set components and profileDetailWithReviewResultDTOList for ProfileInforToPrintDTO.
            cComponentWithPDetailWithReviewResultOfReviewerDTO.setCompetencyComponents(components);
            cComponentWithPDetailWithReviewResultOfReviewerDTO.setCrProfileDetailAndReviewLevelList(profileDetailWithReviewResultDTOList);
            cComponentWithPDetailWithReviewResultOfReviewerDTO.setTotalWeight(profileDetailListAndTotalWeight.getTotalWeight());
            // Add CComponentWPDetailWReviewResultOfReviewerDTO to list of CComponentWPDetailWReviewResultOfReviewerDTO to print.
            cComponentWithPDetailWithReviewResultOfReviewerDTOsList.add(cComponentWithPDetailWithReviewResultOfReviewerDTO);
        }
        return cComponentWithPDetailWithReviewResultOfReviewerDTOsList;
    }

    /**
     * Function is use to set new Job Role for Employee by given approving profile.
     *
     * @param approveBy
     * @param competencyRankingProfilesApprove
     * @param cmtApprove
     */
    public void approveReviewedProfile(Employees approveBy, CompetencyRankingProfiles competencyRankingProfilesApprove, String cmtApprove) {
        // Get new Job Role to approve.
        JobRoles jobRolesToApprove = competencyRankingProfilesApprove.getCompetencyRankingPatterns().getJobRoles();
        // Get CompetencyResults of profile to approve new Job Role.
        CompetencyResults competencyResultsToApprove = competencyRankingProfilesApprove.getCompetencyResults();
        // Get employee of profile to approve new Job Role.
        Employees employeesToApprove = competencyRankingProfilesApprove.getEmployees();
        // Set new Job Role for them.
        competencyResultsToApprove.setApproveRanking(jobRolesToApprove);
        competencyResultsToApprove.setCmtAction(cmtApprove);
        competencyResultsToApprove.setApprovedBy(approveBy);
        employeesToApprove.setJobRoles(jobRolesToApprove);
        // Set Status for profile.
        competencyRankingProfilesApprove.setStatusTypes(StatusTypes.Approved);
        // Save them.
        employeeService.saveEmployee(employeesToApprove);
        competencyResultsService.save(competencyResultsToApprove);
        this.save(competencyRankingProfilesApprove);
    }

    /**
     * Function is use to set Reviewed for given approving profile.
     * Author Son Nguyen
     *
     * @param reviewer
     * @param competencyRankingProfilesApprove
     */
    public void approveSubmittedProfile(Employees reviewer, CompetencyRankingProfiles competencyRankingProfilesApprove, String cmtApprove) {
        // get CompetencyResults of profile to approve reviewed.
        CompetencyResults competencyResultsToApprove = this.getCompetencyResultsByReviewerAndProfile(reviewer, competencyRankingProfilesApprove);
        // Set comment Reviewed for profile
        competencyResultsToApprove.setCmtAction(cmtApprove);
        competencyResultsToApprove.setReviewedDate(new Date());
        // set Status for profile.
        competencyRankingProfilesApprove.setStatusTypes(StatusTypes.Reviewed);
        // Save them.
        competencyResultsService.save(competencyResultsToApprove);
        this.save(competencyRankingProfilesApprove);
    }

    /**
     * Function is use to set Rejected for given reviewed profile.
     *
     * @param rejectBy
     * @param competencyRankingProfilesToReject
     * @param cmtReject
     */
    public void rejectReviewedProfile(Employees rejectBy, CompetencyRankingProfiles competencyRankingProfilesToReject, String cmtReject) {
        // Get CompetencyResults of profile to approve reviewed.
        CompetencyResults competencyResultsToReject = competencyRankingProfilesToReject.getCompetencyResults();
        // Set comment Reviewed for profile
        competencyResultsToReject.setApprovedBy(rejectBy);
        competencyResultsToReject.setCmtAction(cmtReject);
        // Set Status for profile.
        competencyRankingProfilesToReject.setStatusTypes(StatusTypes.Rejected);
        // Save them.
        competencyResultsService.save(competencyResultsToReject);
        this.save(competencyRankingProfilesToReject);
    }

    /**
     * Function is use to reject a profile, then save the result and set status for profile
     * Author Son Nguyen
     *
     * @param reviewer
     * @param competencyRankingProfilesReject
     * @param cmtReject
     */
    public void rejectSubmittedProfile(Employees reviewer, CompetencyRankingProfiles competencyRankingProfilesReject, String cmtReject) {
        // Get CompetencyResults of profile to reject.
        CompetencyResults competencyResultsToReject = this.getCompetencyResultsByReviewerAndProfile(reviewer, competencyRankingProfilesReject);
        // Add comment for result of rejected profile.
        competencyResultsToReject.setCmtAction(cmtReject);
        competencyResultsToReject.setReviewedDate(new Date());
        // Set status for profile.
        competencyRankingProfilesReject.setStatusTypes(StatusTypes.ReviewedFail);
        // Save result and profile.
        competencyResultsService.save(competencyResultsToReject);
        this.save(competencyRankingProfilesReject);
    }

    /**
     * Function is use to Approve a profile by role, Manager => approve rank profile for reviewed profile, Leader => approve submitted profile
     * Author Son Nguyen
     *
     * @param userAccountsLoginDTO
     * @param competencyRankingProfilesToApprove
     * @param cmtApprove
     * @return
     */
    public ResponseEntity<String> approveProfileByRole(UserAccountsLoginDTO userAccountsLoginDTO, CompetencyRankingProfiles competencyRankingProfilesToApprove, String cmtApprove) {
        String approveStatus = "Approve Fail!!!";
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        if (userAccountsLoginDTO.getUserRoles().getUserRoleName().equals("ROLE_LEADER")) {
            if (this.checkConditionProfileToReview(competencyRankingProfilesToApprove)) {
                if (!cmtApprove.equals("")) {
                    Employees approveBy = userAccountsLoginDTO.getEmployees();
                    this.approveSubmittedProfile(approveBy, competencyRankingProfilesToApprove, cmtApprove);
                    approveStatus = "Approve Review Success.";
                    httpStatus = HttpStatus.OK;
                } else {
                    approveStatus = "Please comment before Approve Review Profile !!!";
                    httpStatus = HttpStatus.EXPECTATION_FAILED;
                }
            } else {
                approveStatus = "Can not Approve Profile when some Ranking Profile Detail not Reviewed yet !!!";
                httpStatus = HttpStatus.EXPECTATION_FAILED;
            }
        } else if (userAccountsLoginDTO.getUserRoles().getUserRoleName().equals("ROLE_MANAGER")) {
            if (this.checkConditionProfileToApprove(competencyRankingProfilesToApprove)) {
                if (!cmtApprove.equals("")) {
                    Employees approveBy = userAccountsLoginDTO.getEmployees();
                    this.approveReviewedProfile(approveBy, competencyRankingProfilesToApprove, cmtApprove);
                    approveStatus = "Approve Rank Profile Success.";
                    httpStatus = HttpStatus.OK;
                } else {
                    approveStatus = "Please comment before Approve Rank Profile !!!";
                    httpStatus = HttpStatus.EXPECTATION_FAILED;
                }
            } else {
                approveStatus = "Can not Approve Rank Profile when Profile not Reviewed yet !!!";
                httpStatus = HttpStatus.EXPECTATION_FAILED;
            }
        }
        return new ResponseEntity<>(approveStatus, httpStatus);
    }

    /**
     * Function is use to Reject a profile by role, Manager => reject rank profile for reviewed profile, Leader => reject submitted profile
     *
     * @param userAccountsLoginDTO
     * @param competencyRankingProfilesToReject
     * @param cmtReject
     * @return
     */
    public ResponseEntity<String> rejectProfileByRole(UserAccountsLoginDTO userAccountsLoginDTO, CompetencyRankingProfiles competencyRankingProfilesToReject, String cmtReject) {
        String approveStatus = "Reject Fail!!!";
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        if (userAccountsLoginDTO.getUserRoles().getUserRoleName().equals("ROLE_LEADER")) {
            if (this.checkConditionProfileToReview(competencyRankingProfilesToReject)) {
                if (!cmtReject.equals("")) {
                    Employees rejectBy = userAccountsLoginDTO.getEmployees();
                    this.rejectSubmittedProfile(rejectBy, competencyRankingProfilesToReject, cmtReject);
                    approveStatus = "Reject Review Success.";
                    httpStatus = HttpStatus.OK;
                } else {
                    approveStatus = "Please comment before Reject Review Profile !!!";
                    httpStatus = HttpStatus.EXPECTATION_FAILED;
                }
            } else {
                approveStatus = "Can not Reject Profile when some Ranking Profile Detail not Reviewed yet !!!";
                httpStatus = HttpStatus.EXPECTATION_FAILED;
            }
        } else if (userAccountsLoginDTO.getUserRoles().getUserRoleName().equals("ROLE_MANAGER")) {
            if (this.checkConditionProfileToApprove(competencyRankingProfilesToReject)) {
                if (!cmtReject.equals("")) {
                    Employees rejectBy = userAccountsLoginDTO.getEmployees();
                    this.rejectReviewedProfile(rejectBy, competencyRankingProfilesToReject, cmtReject);
                    approveStatus = "Reject Rank Profile Success.";
                    httpStatus = HttpStatus.OK;
                } else {
                    approveStatus = "Please comment before Reject Rank Profile !!!";
                    httpStatus = HttpStatus.EXPECTATION_FAILED;
                }
            } else {
                approveStatus = "Can not Reject Profile when Profile not Reviewed yet !!!";
                httpStatus = HttpStatus.EXPECTATION_FAILED;
            }
        }
        return new ResponseEntity<>(approveStatus, httpStatus);
    }

    /**
     * Function to find List index of competency component that have competency ranking profile detail not reviewed yet.
     *
     * @param competencyRankingProfiles
     * @return
     */
    public List<Integer> getListIndexUnreviewedCComponent(CompetencyRankingProfiles competencyRankingProfiles) {
        List<CompetencyComponents> competencyComponentsList = competencyRankingProfiles.getCompetencyRankingPatterns().getCompetencyComponentsList();
        List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList = competencyRankingProfiles.getCompetencyRankingProfileDetailsList();
        List<Integer> listIndexUnreviewedCComponent = new ArrayList<>();
        int continuesLoop = 0;
        for (int i = 0; i < competencyComponentsList.size(); i++) {
            for (int j = continuesLoop; j < competencyRankingProfileDetailsList.size(); j++) {
                if (competencyRankingProfileDetailsList.get(j).getCompetencyRankingPatternDetails().getCompetencyComponentDetails().getCompetencyComponents().getComponentId()
                        == competencyComponentsList.get(i).getComponentId()) {
                    try {
                        competencyRankingProfileDetailsList.get(j).getCompetencyReviewRankings().getProficiencyLevels().getProficiencyLevelId();
                    } catch (NullPointerException e) {
                        listIndexUnreviewedCComponent.add(i);
                        continuesLoop = j;
                        break;
                    }
                }
            }
        }
        return listIndexUnreviewedCComponent;
    }

    /**
     * Function to get members profiles to show in MemberProfile Page base on role of account logged.
     *
     * @param conditionOfFilterMemberProfilesDTO
     * @param userAccountsLoginDTO
     * @param currentPage
     * @return
     */
    public MembersProfilesDTO getMembersProfiles(ConditionOfFilterMemberProfilesDTO conditionOfFilterMemberProfilesDTO, UserAccountsLoginDTO userAccountsLoginDTO,
                                                 Integer currentPage) {
        MembersProfilesDTO membersProfilesDTO = new MembersProfilesDTO();
        ResultOfFilterMemberProfileDTO resultOfFilterMemberProfileDTO =
                this.getMemberProfileByFilter(conditionOfFilterMemberProfilesDTO, userAccountsLoginDTO, currentPage);
        List<PeriodPattern> periodPatternList = periodPatternService.findAllByOrderByYearAscNameAsc();
        List<StatusTypes> statusTypesList = null;
        List<JobRoles> jobRolesList = null;
        List<Domains> domainsList = null;
        if (userAccountsLoginDTO.getUserRoles().getUserRoleName().equals("ROLE_LEADER")) {
            statusTypesList = Arrays.asList(StatusTypes.Submitted, StatusTypes.Reviewed, StatusTypes.ReviewedFail, StatusTypes.Reviewing);
            jobRolesList = jobRolesService.findAllByDomains_DomainId(userAccountsLoginDTO.getEmployees().getDomains().getDomainId());
        } else if (userAccountsLoginDTO.getUserRoles().getUserRoleName().equals("ROLE_MANAGER")) {
            statusTypesList = Arrays.asList(StatusTypes.Reviewed, StatusTypes.Approved, StatusTypes.Rejected);
            jobRolesList = jobRolesService.findAllDomainNameGBName();
            domainsList = domainService.getAllDomains();
        }
        membersProfilesDTO.setMemberRankingProfileList(resultOfFilterMemberProfileDTO.getCompetencyRankingProfilesFilterList());
        membersProfilesDTO.setDomainsList(domainsList);
        membersProfilesDTO.setJobRolesList(jobRolesList);
        membersProfilesDTO.setPeriodPatternList(periodPatternList);
        membersProfilesDTO.setStatusTypesList(statusTypesList);
        membersProfilesDTO.setListPage(resultOfFilterMemberProfileDTO.getListPage());
        membersProfilesDTO.setCurrentPage(currentPage);
        return membersProfilesDTO;
    }


    public List<CompetencyRankingProfileDetailsDTO> getCompetencyRankingProfileDetails(String componentName, Integer profileId, UserAccountsLoginDTO userAccountsLoginDTO) {
        CompetencyComponents components = competencyComponentsService.findByComponentName(componentName);
        List<CompetencyRankingProfileDetails> competencyRankingProfileDetails =
                competencyRankingProfileDetailsService.findAllByCompetencyRankingPatternDetails_CompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingProfiles_RankingProfileId(
                        components.getComponentId(), profileId);
        List<CompetencyRankingProfileDetailsDTO> competencyRankingProfileDetailsDTOList = new ArrayList<>();
        for (CompetencyRankingProfileDetails e : competencyRankingProfileDetails) {
            CompetencyRankingProfileDetailsDTO competencyRankingProfileDetailsDTO = new CompetencyRankingProfileDetailsDTO();
            int maxLevelOfCompetencyRankingProfileDetails = e.getProficiencyLevels().getProficiencyLevelTypes().getProficiencyLevelsList().
                    get(e.getProficiencyLevels().getProficiencyLevelTypes().getProficiencyLevelsList().size() - 1).getProficiencyLevel();
            int reviewLevel = 0;
            // Calculate point for details for Leader
            if (!userAccountsLoginDTO.getUserRoles().getUserRoleName().equals("ROLE_MANAGER")) {
                int currentLevel = proficiencyLevelsService.findProficiencyLevelsById(e.getProficiencyLevels().getProficiencyLevelId()).getProficiencyLevel();
                float pointOfCompetencyRankingPatternDetails =
                        (float) ((currentLevel * e.getCompetencyRankingPatternDetails().getRankingWeight() * 1.0) / maxLevelOfCompetencyRankingProfileDetails);
                competencyRankingProfileDetailsDTO.setPointDetails(formatNicelyFloat(pointOfCompetencyRankingPatternDetails));
                if (e.getCompetencyReviewRankings().getProficiencyLevels() != null) {
                    reviewLevel = proficiencyLevelsService.findProficiencyLevelsById(e.getCompetencyReviewRankings().getProficiencyLevels().getProficiencyLevelId()).getProficiencyLevel();
                    float reviewPointOfCompetencyRankingPatternDetails = (float) ((reviewLevel * e.getCompetencyRankingPatternDetails().getRankingWeight() * 1.0) / maxLevelOfCompetencyRankingProfileDetails);
                    competencyRankingProfileDetailsDTO.setReviewPointDetails(formatNicelyFloat(reviewPointOfCompetencyRankingPatternDetails));
                } else {
                    competencyRankingProfileDetailsDTO.setReviewPointDetails("--");
                }
            } else {
                // Details point of pattern
                int currentLevel = proficiencyLevelsService.findProficiencyLevelsById(e.getCompetencyRankingPatternDetails().getRankingLevelRequirementPatterns().getProficiencyLevels().getProficiencyLevelId()).getProficiencyLevel();
                float requirePointOfCompetencyRankingPatternDetails = (float) ((currentLevel * e.getCompetencyRankingPatternDetails().getRankingWeight() * 1.0) / maxLevelOfCompetencyRankingProfileDetails);
                competencyRankingProfileDetailsDTO.setRequirePointDetails(formatNicelyFloat(requirePointOfCompetencyRankingPatternDetails));
                // Review point
                reviewLevel = proficiencyLevelsService.findProficiencyLevelsById(e.getCompetencyReviewRankings().getProficiencyLevels().getProficiencyLevelId()).getProficiencyLevel();
                float reviewPointOfCompetencyRankingPatternDetails = (float) ((reviewLevel * e.getCompetencyRankingPatternDetails().getRankingWeight() * 1.0) / maxLevelOfCompetencyRankingProfileDetails);
                competencyRankingProfileDetailsDTO.setReviewPointDetails(formatNicelyFloat(reviewPointOfCompetencyRankingPatternDetails));
            }
            competencyRankingProfileDetailsDTO.setName(e.getCompetencyRankingPatternDetails().getCompetencyComponentDetails().getComponentDetailName());
            competencyRankingProfileDetailsDTO.setRankingProfileDetailId(e.getRankingProfileDetailId());
            competencyRankingProfileDetailsDTOList.add(competencyRankingProfileDetailsDTO);
        }
        return competencyRankingProfileDetailsDTOList;
    }
//    /**
//     * Function to get Employee Profiles DTO by given employeeId
//     * @param employeeId
//     * @return
//     */
//    public EmployeeProfilesDTO getEmployeeProfilesDTOToApprove(int employeeId){
//        EmployeeProfilesDTO employeeProfilesDTO = new EmployeeProfilesDTO();
//        employeeProfilesDTO.setEmployees(employeeService.findById(employeeId));
//        employeeProfilesDTO.setCompetencyRankingProfilesList(this.findListReviewedCompetencyRankingProfilesByEmployeeId(employeeId));
//        return employeeProfilesDTO;
//    }

    public ProfileDTO convertDTO(CompetencyRankingProfiles competencyRankingProfiles) {
        return ProfileMapper.INSTANCE.toDTO(competencyRankingProfiles);
    }
}
