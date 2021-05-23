package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.JobRoles;
import com.fa.training.mockproject.entities.PeriodPattern;
import com.fa.training.mockproject.entities.dto.PatternDTO;
import com.fa.training.mockproject.entities.dto.ProfileDTO;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.enumeric.StatusTypes;
import com.fa.training.mockproject.services.*;
import com.fa.training.mockproject.services.mapper.PatternMapper;
import com.fa.training.mockproject.services.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

@Controller
@RequestMapping("my-profiles")
@SessionAttributes("user")
public class MyProfilesController {

    @Autowired
    private CompetencyRankingProfilesService competencyRankingProfilesService;

    @Autowired
    private CompetencyRankingPatternsService competencyRankingPatternsService;

    @Autowired
    private UserAccountsService userAccountsService;

    @Autowired
    private PeriodPatternService periodPatternService;

    @Autowired
    private JobRolesService jobRolesService;

    /**
     * Show list Profiles
     *
     * @param principal
     * @param modelAndView
     * @return
     */
    @GetMapping
    public ModelAndView index(Principal principal, ModelAndView modelAndView) {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(principal.getName());
        List<PeriodPattern> periodPatternList = periodPatternService.findAllByOrderByYearAscNameAsc();
        List<StatusTypes> statusTypesList = new ArrayList<>(EnumSet.allOf(StatusTypes.class));
        List<JobRoles> jobRolesList = jobRolesService.findAllByDomains_DomainId(userAccountsLoginDTO.getEmployees().getDomains().getDomainId());
        modelAndView.addObject("competencyRankingProfilesPage", competencyRankingProfilesService.findAllByNoFilter(userAccountsLoginDTO.getEmployees().getEmployeeId(), 0));
        modelAndView.addObject("periodPatternList", periodPatternList);
        modelAndView.addObject("statusTypesList", statusTypesList);
        modelAndView.addObject("jobRolesList", jobRolesList);
        modelAndView.addObject("user", userAccountsLoginDTO);
        modelAndView.setViewName("myprofiles/index");
        return modelAndView;
    }

    /**
     * Create profile page
     *
     * @param modelAndView
     * @return
     */
    @GetMapping(value = "create")
    public ModelAndView createProfile(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO, ModelAndView modelAndView) {
        modelAndView.addObject("periodPatternList", periodPatternService.findAllByOrderByYearAscNameAsc());
        modelAndView.addObject("competencyRankingPatternsList", competencyRankingPatternsService.findAllByJobRoles_Domains_DomainIdAndActiveStatus(userAccountsLoginDTO.getEmployees().getJobRoles().getDomains().getDomainId(), ActiveStatus.Activated));
        modelAndView.setViewName("myprofiles/createprofile");
        return modelAndView;
    }

    /**
     * Check Profile exist to save
     *
     * @param title
     * @param competencyPatternId
     * @param rankingProfileId
     * @return
     */
    @PostMapping("check")
    @ResponseBody
    public boolean checkProfile(@RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "patternId", required = false) Integer competencyPatternId,
                                @RequestParam(value = "rankingProfileId", required = false) Integer rankingProfileId) {
        boolean check = false;
        if (competencyPatternId != null) {
            CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyPatternId);
            if (competencyRankingProfilesService.
                    existsByProfileTitleAndCompetencyRankingPatterns_PeriodPattern_PeriodIdAndCompetencyRankingPatterns_JobRoles_JobRoleIdAndCompetencyRankingPatterns_JobRoles_Domains_DomainId
                            (title, competencyRankingPatterns.getPeriodPattern().getPeriodId(), competencyRankingPatterns.getJobRoles().getJobRoleId(), competencyRankingPatterns.getJobRoles().getDomains().getDomainId())) {
                check = true;
            }
        } else if (rankingProfileId != null) {
            CompetencyRankingProfiles competencyRankingProfiles = competencyRankingProfilesService.findById(rankingProfileId);
            if (competencyRankingProfilesService.
                    existsByProfileTitleAndCompetencyRankingPatterns_PeriodPattern_PeriodIdAndCompetencyRankingPatterns_JobRoles_JobRoleIdAndCompetencyRankingPatterns_JobRoles_Domains_DomainId
                            (title, competencyRankingProfiles.getCompetencyRankingPatterns().getPeriodPattern().getPeriodId(), competencyRankingProfiles.getCompetencyRankingPatterns().getJobRoles().getJobRoleId(), competencyRankingProfiles.getCompetencyRankingPatterns().getJobRoles().getDomains().getDomainId())) {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    /**
     * JSON Get List Pattern
     *
     * @return
     */
    @GetMapping("list-pattern/{periodId}")
    @ResponseBody
    public ResponseEntity<List<PatternDTO>> getListPattern(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO,
                                                           @PathVariable(value = "periodId", required = false) Long periodId) {
        List<PatternDTO> patternDTOList = new ArrayList<>();
        List<CompetencyRankingPatterns> competencyRankingPatternsList = new ArrayList<>();
        if (periodId == null) {
            competencyRankingPatternsList = competencyRankingPatternsService.findAllByJobRoles_Domains_DomainIdAndActiveStatus
                    (userAccountsLoginDTO.getEmployees().getDomains().getDomainId(), ActiveStatus.Activated);
        } else {
            competencyRankingPatternsList =
                    competencyRankingPatternsService.findAllByJobRoles_Domains_DomainIdAndPeriodPattern_PeriodIdAndActiveStatus
                            (userAccountsLoginDTO.getEmployees().getDomains().getDomainId(), periodId, ActiveStatus.Activated);
        }
        competencyRankingPatternsList.forEach(competencyRankingPatterns -> {
            PatternDTO patternDTO = PatternMapper.INSTANCE.toDTO(competencyRankingPatterns);
            patternDTOList.add(patternDTO);
        });
        return new ResponseEntity<>(patternDTOList, HttpStatus.OK);
    }

    /**
     * JSON Delete profile by Id
     *
     * @param id
     * @param principal
     * @param
     * @return
     */
    @GetMapping("delete/{id}")
    @ResponseBody
    public ResponseEntity<Page<ProfileDTO>> ProfileDeleted(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                           @PathVariable("id") int id, Principal principal) {
        competencyRankingProfilesService.deleteById(id);
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(principal.getName());
        Page<ProfileDTO> profileDTOPage = competencyRankingProfilesService.findAllByNoFilter(userAccountsLoginDTO.getEmployees().getEmployeeId(), page);
        return new ResponseEntity<>(profileDTOPage, HttpStatus.OK);
    }

    /**
     * Copy profile
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("copy/{id}")
    public String profileCopy(@PathVariable("id") int id, Model model) {
        ProfileDTO profileDTO = ProfileMapper.INSTANCE.toDTO(competencyRankingProfilesService.findById(id));
        model.addAttribute("periodPatternList", periodPatternService.getALlListPeriodPattern());
        model.addAttribute("rankingProfiles", profileDTO);
        return "myprofiles/copyprofile";
    }

    /**
     * Review Profile
     *
     * @return
     */
    @GetMapping("reviewed/{id}")
    public String reviewProfile(@PathVariable("id") int id, Model model) {
        return "myprofiles/profilereviewedsummary";
    }

    /**
     * Submit Profile
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("submitted/{id}")
    public String rejectProfile(@PathVariable("id") int id, Model model) {
        CompetencyRankingProfiles competencyRankingProfiles = competencyRankingProfilesService.findById(id);
        if (competencyRankingProfiles.getStatusTypes() == StatusTypes.Completed) {
            competencyRankingProfiles.setStatusTypes(StatusTypes.Submitted);
            competencyRankingProfiles.setSubmittedDate(new Date());
            model.addAttribute("message", "Profile has been submit!");
        } else if (competencyRankingProfiles.getStatusTypes() == StatusTypes.Submitted) {
            competencyRankingProfiles.setStatusTypes(StatusTypes.Completed);
            competencyRankingProfiles.setSubmittedDate(new Date());
            model.addAttribute("message", "Profile has been reject!");
        }
        competencyRankingProfilesService.save(competencyRankingProfiles);
        return "redirect:/my-profiles";
    }

    /**
     * Get list Profile
     *
     * @param principal
     * @param title
     * @param periodId
     * @param status
     * @param rolesId
     * @param page
     * @return
     */
    @GetMapping("list-profile/{page}")
    @ResponseBody
    public ResponseEntity<Page<ProfileDTO>> search(Principal principal,
                                                   @RequestParam(value = "title", defaultValue = "", required = false) String title,
                                                   @RequestParam(value = "period", defaultValue = "0", required = false) Long periodId,
                                                   @RequestParam(value = "createdDate", defaultValue = "", required = false) String created,
                                                   @RequestParam(value = "status", defaultValue = "", required = false) String status,
                                                   @RequestParam(value = "roles", defaultValue = "0", required = false) Integer rolesId,
                                                   @PathVariable(value = "page") Integer page) throws ParseException {
        if ("".equals(title)) {
            title = null;
        }
        if (0 == periodId) {
            periodId = null;
        }
        if ("".equals(created)) {
            created = null;
        }
        if ("".equals(status)) {
            status = null;
        }
        if (0 == rolesId) {
            rolesId = null;
        }
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(created);
        } catch (Exception e) {
            date = null;
        }
        int IdUserAccountsLoginDTO = userAccountsService.findByEmail(principal.getName()).getEmployees().getEmployeeId();
        Page<ProfileDTO> profileDTOPage =
                competencyRankingProfilesService.findAllByFilter(title, periodId, date, status, rolesId, IdUserAccountsLoginDTO, page);
        return new ResponseEntity<>(profileDTOPage, HttpStatus.OK);
    }

    @ModelAttribute
    public UserAccountsLoginDTO user(@ModelAttribute("user") UserAccountsLoginDTO userAccountsLoginDTO) {
        return userAccountsLoginDTO;
    }
}
