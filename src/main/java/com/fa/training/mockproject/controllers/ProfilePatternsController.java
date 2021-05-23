package com.fa.training.mockproject.controllers;

import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.entities.dto.PatternDTO;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.enumeric.EvidenceTypes;
import com.fa.training.mockproject.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

@RestController
@RequestMapping("/profile-pattern")
@SessionAttributes("user")
public class ProfilePatternsController {

    @Autowired
    private PeriodPatternService periodPatternService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserAccountsService userAccountsService;

    @Autowired
    private CompetencyRankingPatternsService competencyRankingPatternsService;

    @Autowired
    private JobRolesService jobRolesService;

    @Autowired
    private CompetencyComponentsService competencyComponentsService;

    @Autowired
    private DivideWeightController divideWeightController;

    /*===============================INDEX==============================*/

    /**
     * Index Pattern Page shows Pattern List
     *
     * @return
     */
    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        List<PeriodPattern> periodPatternList = periodPatternService.findAllByOrderByYearAscNameAsc();
        List<ActiveStatus> activeStatusList = new ArrayList<>(EnumSet.allOf(ActiveStatus.class));

        modelAndView.addObject("patternDTOList", competencyRankingPatternsService.findAllCompetencyRankingPatternsNoFilter(0));
        modelAndView.addObject("periodPatternList", periodPatternList);
        modelAndView.addObject("domainNameList", this.getDomainList());
        modelAndView.addObject("jobRolesNameList", this.getJobRolesNameList());
        modelAndView.addObject("activeStatusList", activeStatusList);
        modelAndView.setViewName("profilepatterns/index");
        return modelAndView;
    }

    /**
     * Show name of period and list years (ten years from current year)
     *
     * @param periodPattern
     * @param duplicated
     * @return
     */
    @GetMapping("createPeriodPattern")
    public ModelAndView createProfilePattern(@ModelAttribute("period") PeriodPattern periodPattern,
                                             @RequestParam(value = "duplicated", defaultValue = "false") String duplicated) {

        ModelAndView modelAndView = new ModelAndView();

        List<PeriodPattern> periodPatternList = periodPatternService.findAllByOrderByYearAscNameAsc();

        modelAndView.addObject("period", new PeriodPattern());
        modelAndView.addObject("listPeriodPattern", periodPatternList);
        modelAndView.addObject("duplicated", duplicated);
        modelAndView.addObject("actionStatus", "Create");
        modelAndView.addObject("actionExist", "Back");
        modelAndView.setViewName("profilepatterns/createperiodpattern");
        return modelAndView;
    }

    /**
     * Do process create period
     *
     * @param periodPattern
     * @param year
     * @return
     */
    @PostMapping("createPeriod")
    public ModelAndView createPeriod(@ModelAttribute("period") PeriodPattern periodPattern,
                                     @RequestParam("year") String year) {

        ModelAndView modelAndView = new ModelAndView();

        List<PeriodPattern> periodPatternListFindByNameList = periodPatternService.getAllPeriodByName(periodPattern.getName());
        // Check period which will be created is duplicated or not duplicated base on year input
        boolean checkExistPeriod = false;
        for (PeriodPattern pp : periodPatternListFindByNameList) {
            if (pp.getYear().equals(year)) {
                checkExistPeriod = true;
                break;
            }
        }
        if (checkExistPeriod == false) {
            periodPattern.setYear(year);
            periodPatternService.savePeriodPattern(periodPattern);

            List<PeriodPattern> periodPatternList = periodPatternService.findAllByOrderByYearAscNameAsc();

            modelAndView.addObject("listPeriodPattern", periodPatternList);
            modelAndView.addObject("period", new PeriodPattern());
        } else {
            modelAndView.addObject("duplicated", "true");
        }
        modelAndView.setViewName("redirect:createPeriodPattern");
        return modelAndView;
    }

    /**
     * Support update period, only permit change year of period
     *
     * @param periodId
     * @param periodPattern
     * @return
     */
    @GetMapping("editPeriod/{id}")
    public ModelAndView editPeriod(@PathVariable("id") Long periodId,
                                   @ModelAttribute("period") PeriodPattern periodPattern) {

        ModelAndView modelAndView = new ModelAndView();

        PeriodPattern periodPatternFromDB = periodPatternService.getPeriodPatternById(periodId);
        List<PeriodPattern> periodPatternList = periodPatternService.findAllByOrderByYearAscNameAsc();

        modelAndView.addObject("period", periodPatternFromDB);
        modelAndView.addObject("listPeriodPattern", periodPatternList);
        modelAndView.addObject("actionStatus", "Update");
        modelAndView.addObject("actionExist", "Cancel");

        modelAndView.setViewName("profilepatterns/createperiodpattern");
        return modelAndView;
    }

    /*===============================CREATE PATTERN==============================*/

    /**
     * Show index page to create pattern
     *
     * @param competencyRankingPatterns
     * @param duplicated
     * @return
     */
    @GetMapping("createProfilePattern")
    public ModelAndView goToCreatePattern(
            @ModelAttribute("pattern") CompetencyRankingPatterns competencyRankingPatterns,
            @RequestParam(value = "duplicated", defaultValue = "false") String duplicated) {

        ModelAndView modelAndView = new ModelAndView();

        List<PeriodPattern> periodPatternList = periodPatternService.findAllByOrderByYearAscNameAsc();

        modelAndView.addObject("periods", periodPatternList);
        modelAndView.addObject("domainList", this.getDomainList());
        modelAndView.addObject("pattern", new CompetencyRankingPatterns());
        modelAndView.addObject("duplicated", duplicated);

        modelAndView.setViewName("profilepatterns/createprofilepattern");
        return modelAndView;
    }

    /**
     * Do process create pattern
     *
     * @param componentIdList
     * @param domainName
     * @param periodId
     * @param jobRolesName
     * @return
     */
    @PostMapping("createPattern")
    public ModelAndView createProfilePattern(
            @RequestParam(value = "componentCreatePatternPage", required = false) List<Integer> componentIdList,
            @RequestParam("domainName") String domainName,
            @RequestParam("period") Long periodId,
            @RequestParam("jobRolesName") String jobRolesName) {

        ModelAndView modelAndView = new ModelAndView();
        // componentIdList is used to make sure the user have selected component for pattern
        if (componentIdList != null) {
            PeriodPattern periodPattern = periodPatternService.getPeriodPatternById(periodId);
            Domains domains = domainService.findDomainByDomainName(domainName);
            JobRoles jobRoles = jobRolesService.findByDomains_DomainIdAndJobRoleName(domains.getDomainId(), jobRolesName);
            List<CompetencyRankingPatterns> competencyRankingPatternsList = competencyRankingPatternsService.getAllByPeriodPatternPeriodId(periodPattern.getPeriodId());
            // Check the pattern which will be created is already exist or not
            boolean checkCompetencyRankingPatternsExist = false;
            for (CompetencyRankingPatterns CRP : competencyRankingPatternsList) {
                if (CRP.getJobRoles().getJobRoleName().equals(jobRolesName) && CRP.getJobRoles().getDomains().getDomainName().equals(domainName)) {
                    checkCompetencyRankingPatternsExist = true;
                    break;
                }
            }
            //
            if (checkCompetencyRankingPatternsExist == true) {
                modelAndView.addObject("duplicated", "true");
                modelAndView.setViewName("redirect:createProfilePattern");
            } else {
                // Create new pattern
                CompetencyRankingPatterns competencyRankingPatterns = new CompetencyRankingPatterns();
                competencyRankingPatterns.setPeriodPattern(periodPattern);
                competencyRankingPatterns.setJobRoles(jobRoles);
                competencyRankingPatterns.setActiveStatus(ActiveStatus.InProcess);
                // Handle divide ranking weight for each component and details -- and save new pattern
                divideWeightController.supportDivideForComponentAndRankingPatternDetails(competencyRankingPatterns, componentIdList);
                // Get domain after save
                CompetencyRankingPatterns competencyRankingPatternsAfterSave = competencyRankingPatternsService.findByPeriodIdAndDomainIdAndJobRolesId(periodPattern.getPeriodId(), domains.getDomainId(), jobRoles.getJobRoleId());
                modelAndView.setViewName("redirect:/profile-pattern/new-profile-pattern" + "/" + competencyRankingPatternsAfterSave.getCompetencyPatternId() + "/" + competencyRankingPatternsAfterSave.getCompetencyComponentsList().get(0).getComponentId() + "/" + 1);
            }
        } else {
            modelAndView.setViewName("redirect:createProfilePattern");
        }
        return modelAndView;
    }

    /**
     * Delete pattern
     *
     * @param competencyPatternId
     * @return
     */
    @DeleteMapping("deleteProfilePattern/{competencyPatternId}")
    public ResponseEntity<Void> deleteProfilePatternAjax(@PathVariable("competencyPatternId") int competencyPatternId) {
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyPatternId);
        competencyRankingPatternsService.delete(competencyRankingPatterns);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Show copy page pattern
     *
     * @param competencyPatternId
     * @return
     */
    @GetMapping("copyProfilePattern/{competencyPatternId}")
    public ModelAndView goToCopyPatternPage(@PathVariable("competencyPatternId") int competencyPatternId) {
        ModelAndView modelAndView = new ModelAndView();
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyPatternId);
        List<CompetencyRankingPatterns> competencyRankingPatternsList = competencyRankingPatternsService.getAllByJobRoleAndDomain(competencyRankingPatterns);
        List<PeriodPattern> periodPatternList = new ArrayList<>();
        List<PeriodPattern> periodPatternIsUsed = new ArrayList<>();
        for (CompetencyRankingPatterns e : competencyRankingPatternsList) {
            periodPatternIsUsed.add(e.getPeriodPattern());
        }
        List<PeriodPattern> periodPatternListFromData = periodPatternService.findAllByOrderByYearAscNameAsc();
        //
        for (PeriodPattern periodPattern : periodPatternListFromData) {
            if (!periodPatternIsUsed.contains(periodPattern)) {
                periodPatternList.add(periodPattern);
            }
        }
        modelAndView.addObject("competencyRankingPatterns", competencyRankingPatterns);
        modelAndView.addObject("periods", periodPatternList);

        modelAndView.setViewName("profilepatterns/copyprofilepattern");
        return modelAndView;
    }

    /**
     * Do process copy pattern (structure's pattern, pattern name, job role, period) -- only show permit select period
     * Number of periods can select base on number of periods is created subtract with number of patterns same name and job role
     *
     * @param competencyPatternId
     * @param periodId
     * @param jobRolesName
     * @return
     */
    @PostMapping("copyProfilePattern")
    public ModelAndView copyPattern(@RequestParam("patternId") Integer competencyPatternId,
                                    @RequestParam("period") Long periodId,
                                    @RequestParam("jobRolesName") String jobRolesName) {
        ModelAndView modelAndView = new ModelAndView();
        CompetencyRankingPatterns competencyRankingPatterns = competencyRankingPatternsService.findById(competencyPatternId);
        CompetencyRankingPatterns competencyRankingPatternsCopy = new CompetencyRankingPatterns();
        List<CompetencyComponents> competencyComponentsListCopy = new ArrayList<>();
        for (CompetencyComponents e : competencyRankingPatterns.getCompetencyComponentsList()) {
            CompetencyComponents competencyComponentsCopy = competencyComponentsService.findById(e.getComponentId());
            competencyComponentsListCopy.add(competencyComponentsCopy);
        }
        // Set jobRoles for competencyRankingPatternsCopy
        JobRoles jobRolesCopy = jobRolesService.findByDomains_DomainIdAndJobRoleName(competencyRankingPatterns.getJobRoles().getDomains().getDomainId(), jobRolesName);
        competencyRankingPatternsCopy.setJobRoles(jobRolesCopy);
        // Set period for competencyRankingPatternsCopy
        PeriodPattern periodPatternCopy = periodPatternService.getPeriodPatternById(periodId);
        competencyRankingPatternsCopy.setPeriodPattern(periodPatternCopy);
        // Get all ranking pattern details
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetails = competencyRankingPatterns.getCompetencyRankingPatternDetailsList();
        List<CompetencyRankingPatternDetails> competencyRankingPatternDetailsListCopy = new ArrayList<>();
        for (CompetencyRankingPatternDetails e : competencyRankingPatternDetails) {
            CompetencyRankingPatternDetails competencyRankingPatternDetailCopy = new CompetencyRankingPatternDetails();
            // copy component with id = 2/3/4/5
            if (e.getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == 2 ||
                    e.getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == 3 ||
                    e.getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == 4 ||
                    e.getCompetencyComponentDetails().getCompetencyComponents().getComponentId() == 5) {
                //
                competencyRankingPatternDetailCopy.setRankingWeight(e.getRankingWeight());
                competencyRankingPatternDetailCopy.setEvidenceTypes(e.getEvidenceTypes());
                competencyRankingPatternDetailCopy.setCompetencyComponentDetails(e.getCompetencyComponentDetails());
                RankingLevelRequirementPatterns rankingLevelRequirementPatternsCopy = new RankingLevelRequirementPatterns();
                rankingLevelRequirementPatternsCopy.setProficiencyLevels(e.getRankingLevelRequirementPatterns().getProficiencyLevels());
                competencyRankingPatternDetailCopy.setRankingLevelRequirementPatterns(rankingLevelRequirementPatternsCopy);
            } else {
                RankingLevelRequirementPatterns rankingLevelRequirementPatternsCopy = new RankingLevelRequirementPatterns();
                competencyRankingPatternDetailCopy.setRankingWeight(e.getRankingWeight());
                competencyRankingPatternDetailCopy.setRankingLevelRequirementPatterns(rankingLevelRequirementPatternsCopy);
                competencyRankingPatternDetailCopy.setCompetencyComponentDetails(e.getCompetencyComponentDetails());
                competencyRankingPatternDetailCopy.setEvidenceTypes(EvidenceTypes.Optional);
            }
            competencyRankingPatternDetailCopy.setCompetencyRankingPatterns(competencyRankingPatternsCopy);
            competencyRankingPatternDetailsListCopy.add(competencyRankingPatternDetailCopy);
        }
        competencyRankingPatternsCopy.setActiveStatus(ActiveStatus.InProcess);
        competencyRankingPatternsCopy.setCompetencyComponentsList(competencyComponentsListCopy);
        competencyRankingPatternsCopy.setCompetencyRankingPatternDetailsList(competencyRankingPatternDetailsListCopy);
        competencyRankingPatternsService.save(competencyRankingPatternsCopy);
        modelAndView.setViewName("redirect:" + "/profile-pattern");
        return modelAndView;
    }

    /**
     * @param principal
     * @return userAccountsLoginDTO
     */
    @ModelAttribute("user")
    public UserAccountsLoginDTO user(Principal principal) {
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(principal.getName());
        return userAccountsLoginDTO;
    }

    /*======================MANAGE DOMAIN EMPLOYEES=========================*/

    /**
     * Show domain of each employees
     *
     * @return
     */
    @GetMapping("updateDomainEmployee")
    public ModelAndView getListUserAccounts() {
        ModelAndView modelAndView = new ModelAndView();
        List<UserAccounts> userAccountsList = userAccountsService.findAllUserAccounts();
        modelAndView.addObject("userAccountsList", userAccountsList);
        modelAndView.setViewName("profilepatterns/accessdomainemployee");
        return modelAndView;
    }

    /**
     * Do process update domain for employees
     *
     * @param employeeId
     * @param domainName
     * @return
     */
    @PostMapping("updateDomainEmployee")
    public ModelAndView updateEmployeeRoles(@RequestParam("employeeId") int employeeId,
                                            @RequestParam(value = "domainName") String domainName) {
        ModelAndView modelAndView = new ModelAndView();
        Employees employees = employeeService.findById(employeeId);
        Domains domains = domainService.findDomainByDomainName(domainName);
        employees.setDomains(domains);
        employeeService.updateEmployee(employees);
        List<UserAccounts> userAccountsList = userAccountsService.findAllUserAccounts();
        modelAndView.addObject("userAccountsList", userAccountsList);
        modelAndView.setViewName("redirect:updateDomainEmployee");
        return modelAndView;
    }

    /*=======================CREATE PATTERN -- SELECT PERIOD/ DOMAIN/ JOB ROLE================*/

    /**
     * Support for ajax function to create patten process
     * Get all domain name
     *
     * @return list domain's name
     */
    @GetMapping("/show-all_domain-name")
    @ResponseBody
    public ResponseEntity<List<String>> getAllDomainName() {
        List<String> domainNameList = this.getDomainList();
        return new ResponseEntity<>(domainNameList, HttpStatus.OK);
    }

    /**
     * Support for ajax function to create patten
     * Get job role which is not selected
     *
     * @param periodId
     * @param domainName
     * @return list job role's name
     */
    @GetMapping("/get-job-roles/{periodId}/{domainName}")
    @ResponseBody
    public ResponseEntity<List<String>> getJobRolesBaseOnPeriodAndDomain(@PathVariable("periodId") Long periodId,
                                                                         @PathVariable("domainName") String domainName) {
        List<String> jobRolesList = this.getJobRolesNameList();
        Domains domains = domainService.findDomainByDomainName(domainName);
        List<CompetencyRankingPatterns> competencyRankingPatternsList = competencyRankingPatternsService.findCompetencyRankingPatternsByPeriodPattern_PeriodIdAndJobRoles_Domains_DomainId(periodId, domains.getDomainId());
        List<String> jobRolesExist = new ArrayList<>();
        for (CompetencyRankingPatterns e : competencyRankingPatternsList) {
            if (!jobRolesExist.contains(e.getJobRoles().getJobRoleName())) {
                jobRolesExist.add(e.getJobRoles().getJobRoleName());
            }
        }
        List<String> jobRolesRemain = new ArrayList<>();
        for (String e : jobRolesList) {
            if (!jobRolesExist.contains(e)) {
                jobRolesRemain.add(e);
            }
        }
        return new ResponseEntity<>(jobRolesRemain, HttpStatus.OK);
    }

    /*=======================CREATE PERIOD -- SELECT PERIOD/ YEAR================*/

    /**
     * Support for ajax function to create period
     * Get list years is not selected
     *
     * @param periodName
     * @return list year remain
     */
    @GetMapping("select-period-name/{periodName}")
    @ResponseBody
    public ResponseEntity<List<String>> getListYearByPeriodName(@PathVariable("periodName") String periodName) {
        List<String> yearList = this.getYears();
        List<PeriodPattern> periodPatternList = periodPatternService.getAllPeriodByName(periodName);
        List<String> yearListRemain = new ArrayList<>();
        if (0 != periodPatternList.size()) {
            for (String e : yearList) {
                boolean statusExist = false;
                for (PeriodPattern period : periodPatternList) {
                    if (e.equals(period.getYear())) {
                        statusExist = true;
                        break;
                    }
                }
                if (statusExist == false) {
                    yearListRemain.add(e);
                }
            }
        } else {
            yearListRemain = yearList;
        }

        return new ResponseEntity<>(yearListRemain, HttpStatus.OK);
    }

    /*===============================================================*/

    /**
     * Support get all domain name
     *
     * @return list domain name
     */
    @ModelAttribute("domainNameList")
    public List<String> getDomainList() {
        List<Domains> domainsList = domainService.getAllDomains();
        List<String> domainNameList = new ArrayList<>();
        for (Domains domains : domainsList) {
            if ((!domainNameList.contains(domains.getDomainName()) && (!domains.getDomainName().contains("New Account")))) {
                domainNameList.add(domains.getDomainName());
            }
        }
        return domainNameList;
    }

    /**
     * Support get all job role name
     *
     * @return list job role name
     */
    @ModelAttribute("jobRolesNameList")
    public List<String> getJobRolesNameList() {
        List<JobRoles> jobRolesList = jobRolesService.getAll();
        List<String> jobRolesNameList = new ArrayList<>();
        for (JobRoles jr : jobRolesList) {
            if (!jobRolesNameList.contains(jr.getJobRoleName())) {
                jobRolesNameList.add(jr.getJobRoleName());
            }
        }
        return jobRolesNameList;
    }

    /**
     * Support get all component
     *
     * @return list component
     */
    @ModelAttribute("componentList")
    public List<CompetencyComponents> getCompetencyComponentsList() {
        List<CompetencyComponents> competencyComponentsList = competencyComponentsService.getAllCompetencyComponents();
        return competencyComponentsList;
    }

    /*===============================YEAR OF PERIOD==============================*/

    /**
     * Support get list years (ten years) from current years
     *
     * @return list years
     */
    @ModelAttribute("years")
    public List<String> getYears() {
        List<String> years = new ArrayList<>();
        int year = (Calendar.getInstance().get(Calendar.YEAR));
        years.add(String.valueOf(year));
        for (int i = 0; i < 10; i++) {
            year += 1;
            years.add(String.valueOf(year));
        }
        return years;
    }

    //========================SEARCH -PAGE========================

    /**
     * Support search pattern
     *
     * @param title
     * @param domainName
     * @param jobRolesName
     * @param periodId
     * @param status
     * @param page
     * @return PatternDTO
     */
    @GetMapping("list-pattern/{page}")
    @ResponseBody
    public ResponseEntity<Page<PatternDTO>> search(
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "domainName", defaultValue = "") String domainName,
            @RequestParam(value = "jobRolesName", defaultValue = "") String jobRolesName,
            @RequestParam(value = "period", defaultValue = "0") Long periodId,
            @RequestParam(value = "stats", defaultValue = "") String status,
            @PathVariable(value = "page") Integer page) {

        if ("".equals(title)) {
            title = null;
        }
        if ("".equals(domainName)) {
            domainName = null;
        }
        if ("".equals(jobRolesName)) {
            jobRolesName = null;
        }
        if (0 == periodId) {
            periodId = null;
        }
        if ("".equals(status)) {
            status = null;
        }

        Page<PatternDTO> patternDTOPage =
                competencyRankingPatternsService.findAllCompetencyRankingPatternsByFilter(title, domainName, jobRolesName, periodId, status, page);
        return new ResponseEntity<>(patternDTOPage, HttpStatus.OK);
    }
}
