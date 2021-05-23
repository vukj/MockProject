package com.fa.training.mockproject.data;

import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.repositories.*;
import com.fa.training.mockproject.services.PeriodPatternService;
import com.fa.training.mockproject.services.impl.DataSourceServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class CompetencyRankingPattenData {

    @Autowired
    private ProficiencyLevelsRepository proficiencyLevelsRepository;

    @Autowired
    private CompetencyRankingPatternDetailsRepository competencyRankingPatternDetailsRepository;

    @Autowired
    private DomainTypesRepository domainTypesRepository;

    @Autowired
    private DomainsRepository domainsRepository;

    @Autowired
    private JobRolesRepository rankingRolesRepository;

    @Autowired
    private CompetencyRankingPatternsRepository competencyRankingPatternsRepository;

    @Autowired
    private CompetencyComponentsRepository competencyComponentsRepository;

    @Autowired
    private CompetencyComponentDetailsRepository competencyComponentDetailsRepository;

    @Autowired
    private RankingLevelRequirementPatternsRepository rankingLevelRequirementPatternsRepository;

    @Autowired
    private ProficiencyLevelTypesRepository proficiencyLevelTypesRepository;

    @Autowired
    private PeriodPatternService periodPatternService;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private DataSourceServiceImp dataSourceServiceImp;

    public void insertDataCompetencyRankingPatten() {

        // Pattern 1
        // Competency Ranking Patterns Data...................................................
        DomainTypes domainTypes1 = new DomainTypes("Artificial Intelligence");
        domainTypesRepository.save(domainTypes1);

        Domains domains1 = new Domains("Software Engineer", domainTypes1);
        Domains domains2 = new Domains("Technical Engineer", domainTypes1);
        Domains domains3 = new Domains("New Account", domainTypes1);

        PeriodPattern periodPatternFromDB = periodPatternService.getPeriodPatternById(Long.valueOf("1"));
        List<PeriodPattern> periodPatternList = new ArrayList<>();
        periodPatternList.add(periodPatternFromDB);
        domains1.setActiveStatus(ActiveStatus.Activated);
        domains2.setActiveStatus(ActiveStatus.Activated);
        domains3.setActiveStatus(ActiveStatus.Activated);

        PeriodPattern periodPattern = new PeriodPattern();
        periodPattern.setYear("2021");
        periodPattern.setName("S1");

        PeriodPattern periodPattern2 = new PeriodPattern();
        periodPattern2.setYear("2022");
        periodPattern2.setName("S2");

        periodPatternService.savePeriodPattern(periodPattern);
        periodPatternService.savePeriodPattern(periodPattern2);

        JobRoles jobRoles1 = new JobRoles("Fresher", domains1);
        JobRoles jobRoles2 = new JobRoles("Junior", domains1);
        JobRoles jobRoles3 = new JobRoles("Senior", domains1);
        JobRoles jobRoles4 = new JobRoles("Fresher", domains2);
        JobRoles jobRoles5 = new JobRoles("Junior", domains2);
        JobRoles jobRoles6 = new JobRoles("Senior", domains2);
        rankingRolesRepository.save(jobRoles1);
        rankingRolesRepository.save(jobRoles2);
        rankingRolesRepository.save(jobRoles3);
        rankingRolesRepository.save(jobRoles4);
        rankingRolesRepository.save(jobRoles5);
        rankingRolesRepository.save(jobRoles6);

        List<JobRoles> jobRolesList = new ArrayList<>();
        jobRolesList.add(jobRoles1);
        jobRolesList.add(jobRoles2);
        jobRolesList.add(jobRoles3);

        List<JobRoles> jobRolesList1 = new ArrayList<>();
        jobRolesList.add(jobRoles4);
        jobRolesList.add(jobRoles5);
        jobRolesList.add(jobRoles6);

        domains1.setJobRolesList(jobRolesList);
        domainsRepository.save(domains1);

        domains2.setJobRolesList(jobRolesList1);
        domainsRepository.save(domains2);

        domainsRepository.save(domains3);

        Employees manager = employeesRepository.findByEmployeeId(1);
        manager.setDomains(domains2);
        employeesRepository.save(manager);

        Employees leader = employeesRepository.findByEmployeeId(2);
        leader.setDomains(domains1);
        employeesRepository.save(leader);

        Employees leader1 = employeesRepository.findByEmployeeId(3);
        leader1.setDomains(domains2);
        employeesRepository.save(leader1);

        Employees staff = employeesRepository.findByEmployeeId(4);
        staff.setJobRoles(jobRoles1);
        staff.setDomains(domains1);
        employeesRepository.save(staff);

        Employees staff1 = employeesRepository.findByEmployeeId(5);
        staff1.setJobRoles(jobRoles1);
        staff1.setDomains(domains2);
        employeesRepository.save(staff1);

        Employees staff2 = employeesRepository.findByEmployeeId(6);
        staff2.setJobRoles(jobRoles1);
        staff2.setDomains(domains1);
        employeesRepository.save(staff2);

        Employees staff3 = employeesRepository.findByEmployeeId(7);
        staff3.setJobRoles(jobRoles1);
        staff3.setDomains(domains2);
        employeesRepository.save(staff3);

        Employees staff4 = employeesRepository.findByEmployeeId(8);
        staff4.setJobRoles(jobRoles1);
        staff4.setDomains(domains1);
        employeesRepository.save(staff4);

        CompetencyComponents competencyComponents1 = new CompetencyComponents("Responsibilities and Roles");
        competencyComponents1.setComponentWeight(Byte.valueOf("30"));
        CompetencyComponents competencyComponents2 = new CompetencyComponents("Business Contributions");
        competencyComponents2.setComponentWeight(Byte.valueOf("20"));
        CompetencyComponents competencyComponents3 = new CompetencyComponents("Professional Skills");
        competencyComponents3.setComponentWeight(Byte.valueOf("10"));
        CompetencyComponents competencyComponents4 = new CompetencyComponents("Advanced Engineering Skills");
        competencyComponents4.setComponentWeight(Byte.valueOf("10"));
        CompetencyComponents competencyComponents5 = new CompetencyComponents("Non-Engineering Skills");
        competencyComponents5.setComponentWeight(Byte.valueOf("10"));
        CompetencyComponents competencyComponents6 = new CompetencyComponents("Foreign Language Skills");
        competencyComponents6.setComponentWeight(Byte.valueOf("10"));
        CompetencyComponents competencyComponents7 = new CompetencyComponents("Education and Knowledge");
        competencyComponents7.setComponentWeight(Byte.valueOf("10"));

        List<CompetencyComponents> competencyComponentsList = new ArrayList<>();
        competencyComponentsList.add(competencyComponents1);
        competencyComponentsList.add(competencyComponents2);
        competencyComponentsList.add(competencyComponents3);
        competencyComponentsList.add(competencyComponents4);
        competencyComponentsList.add(competencyComponents5);
        competencyComponentsList.add(competencyComponents6);
        competencyComponentsList.add(competencyComponents7);

        List<CompetencyComponents> competencyComponentsList2 = new ArrayList<>();
        competencyComponentsList2.add(competencyComponents1);
        competencyComponentsList2.add(competencyComponents2);
        competencyComponentsList2.add(competencyComponents3);
        competencyComponentsList2.add(competencyComponents4);
        competencyComponentsList2.add(competencyComponents5);
        competencyComponentsList2.add(competencyComponents6);

        for (CompetencyComponents e : competencyComponentsList) {
            competencyComponentsRepository.save(e);
        }

        List<DataSources> dataSourcesList1 = new ArrayList<>();
        dataSourcesList1.add(new DataSources("Unknown"));
        dataSourcesList1.add(new DataSources("Program Manager"));
        dataSourcesList1.add(new DataSources("Project Manager"));
        dataSourcesList1.add(new DataSources("Technical Leader"));
        dataSourcesList1.add(new DataSources("Team Leader"));
        dataSourcesList1.add(new DataSources("Management Manager"));

        List<DataSources> dataSourcesList2 = new ArrayList<>();
        dataSourcesList2.add(new DataSources("Unknown"));
        dataSourcesList2.add(new DataSources("Jira"));
        dataSourcesList2.add(new DataSources("DevOps"));
        dataSourcesList2.add(new DataSources("SRF System"));
        dataSourcesList2.add(new DataSources("PMO"));
        dataSourcesList2.add(new DataSources("CTC"));

        List<DataSources> dataSourcesList3 = new ArrayList<>();
        dataSourcesList3.add(new DataSources("Unknown"));
        dataSourcesList3.add(new DataSources("Jira"));
        dataSourcesList3.add(new DataSources("DevOps"));
        dataSourcesList3.add(new DataSources("SRF System"));
        dataSourcesList3.add(new DataSources("PMO"));
        dataSourcesList3.add(new DataSources("Human Resource"));

        saveDataSourceList(dataSourcesList1);
        saveDataSourceList(dataSourcesList2);
        saveDataSourceList(dataSourcesList3);

        CompetencyComponentDetails competencyComponentDetails[] = new CompetencyComponentDetails[27];

        competencyComponentDetails[1] = new CompetencyComponentDetails(
                "Responsibilities and Duties",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 1),
                competencyComponents1,
                dataSourcesList1
        );

        competencyComponentDetails[2] = new CompetencyComponentDetails(
                "Job Roles",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 2),
                competencyComponents1,
                dataSourcesList1
        );

        competencyComponentDetails[3] = new CompetencyComponentDetails(
                "Organization Contributions",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 3),
                competencyComponents2,
                dataSourcesList2
        );

        competencyComponentDetails[4] = new CompetencyComponentDetails(
                "Project Contributions",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 4),
                competencyComponents2,
                dataSourcesList2
        );

        competencyComponentDetails[5] = new CompetencyComponentDetails(
                "Personal Contributions",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 5),
                competencyComponents2,
                dataSourcesList2
        );

        competencyComponentDetails[6] = new CompetencyComponentDetails(
                "Frameworks and Runtime Engine",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents3,
                dataSourcesList2
        );
        competencyComponentDetails[7] = new CompetencyComponentDetails(
                "Development Tools and Environment",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents3,
                dataSourcesList2
        );
        competencyComponentDetails[8] = new CompetencyComponentDetails(
                "Programming Language",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents3,
                dataSourcesList2
        );

        competencyComponentDetails[9] = new CompetencyComponentDetails(
                "Mobile Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents4,
                dataSourcesList2
        );
        competencyComponentDetails[10] = new CompetencyComponentDetails(
                "Cloud Computing Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents4,
                dataSourcesList2
        );
        competencyComponentDetails[11] = new CompetencyComponentDetails(
                "Specialized Software and Tools",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents4,
                dataSourcesList2
        );
        competencyComponentDetails[12] = new CompetencyComponentDetails(
                "OPTIONAL SKILL LIST",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents4,
                dataSourcesList2
        );

        competencyComponentDetails[13] = new CompetencyComponentDetails(
                "Business or Domain Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents5,
                dataSourcesList3
        );
        competencyComponentDetails[14] = new CompetencyComponentDetails(
                "[Specific] Functional & Application Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents5,
                dataSourcesList3
        );
        competencyComponentDetails[15] = new CompetencyComponentDetails(
                "Product and Service Management Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents5,
                dataSourcesList3
        );
        competencyComponentDetails[16] = new CompetencyComponentDetails(
                "Pre-Sales / Technical Sales Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents5,
                dataSourcesList3
        );
        competencyComponentDetails[17] = new CompetencyComponentDetails(
                "Workplace Skills and Soft Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 6),
                competencyComponents5,
                dataSourcesList3
        );

        competencyComponentDetails[18] = new CompetencyComponentDetails(
                "English Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 7),
                competencyComponents6,
                dataSourcesList3
        );
        competencyComponentDetails[19] = new CompetencyComponentDetails(
                "Japanese Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 7),
                competencyComponents6,
                dataSourcesList3
        );
        competencyComponentDetails[20] = new CompetencyComponentDetails(
                "Chinese Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 7),
                competencyComponents6,
                dataSourcesList3
        );
        competencyComponentDetails[21] = new CompetencyComponentDetails(
                "Korean Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 7),
                competencyComponents6,
                dataSourcesList3
        );
        competencyComponentDetails[22] = new CompetencyComponentDetails(
                "French Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 7),
                competencyComponents6,
                dataSourcesList3
        );
        competencyComponentDetails[23] = new CompetencyComponentDetails(
                "Vietnamese Skills",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 7),
                competencyComponents6,
                dataSourcesList3
        );

        competencyComponentDetails[24] = new CompetencyComponentDetails(
                "Methodologies and Knowledge",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 8),
                competencyComponents7,
                dataSourcesList3
        );

        competencyComponentDetails[25] = new CompetencyComponentDetails(
                "Education and Qualifications",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 9),
                competencyComponents7,
                dataSourcesList3
        );

        competencyComponentDetails[26] = new CompetencyComponentDetails(
                "Professional Certifications",
                proficiencyLevelTypesRepository.findByProficiencyLevelTypeId((short) 10),
                competencyComponents7,
                dataSourcesList3
        );

        for (int i = 1; i < competencyComponentDetails.length; i++) {
            competencyComponentDetailsRepository.save(competencyComponentDetails[i]);
        }

//        CompetencyRankingPatterns competencyRankingPatterns1 = new CompetencyRankingPatterns();
//        competencyRankingPatterns1.setJobRoles(jobRoles3);
//        competencyRankingPatterns1.setPeriodPattern(periodPattern);
//        competencyRankingPatterns1.setCompetencyComponentsList(competencyComponentsList);
//        competencyRankingPatterns1.setActiveStatus(ActiveStatus.Activated);
//        competencyRankingPatternsRepository.save(competencyRankingPatterns1);
//
//        RankingLevelRequirementPatterns rankingLevelRequirementPatterns[] = new RankingLevelRequirementPatterns[27];
//        rankingLevelRequirementPatterns[1] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(6),
//                proficiencyLevelsRepository.findByProficiencyLevelId(7)
//        );
//
//        rankingLevelRequirementPatterns[2] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(12),
//                proficiencyLevelsRepository.findByProficiencyLevelId(13)
//        );
//
//        rankingLevelRequirementPatterns[3] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(15),
//                proficiencyLevelsRepository.findByProficiencyLevelId(19)
//        );
//
//        rankingLevelRequirementPatterns[4] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(25),
//                proficiencyLevelsRepository.findByProficiencyLevelId(26)
//        );
//
//        rankingLevelRequirementPatterns[5] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(30),
//                proficiencyLevelsRepository.findByProficiencyLevelId(32)
//        );
//
//        rankingLevelRequirementPatterns[6] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(34),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[7] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(36),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[8] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(36),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//
//        rankingLevelRequirementPatterns[9] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(35),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[10] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(35),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[11] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(38),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[12] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(39),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//
//        rankingLevelRequirementPatterns[13] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(36),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[14] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(38),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[15] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(36),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[16] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(35),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns[17] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(35),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//
//        rankingLevelRequirementPatterns[18] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(40),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns[19] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(45),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns[20] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(45),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns[21] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(42),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns[22] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(45),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns[23] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(45),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//
//        rankingLevelRequirementPatterns[24] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(49),
//                proficiencyLevelsRepository.findByProficiencyLevelId(53)
//        );
//
//        rankingLevelRequirementPatterns[25] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(55),
//                proficiencyLevelsRepository.findByProficiencyLevelId(59)
//        );
//
//        rankingLevelRequirementPatterns[26] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(61),
//                proficiencyLevelsRepository.findByProficiencyLevelId(64)
//        );
//
//        for (int i = 1; i < rankingLevelRequirementPatterns.length; i++) {
//            rankingLevelRequirementPatternsRepository.save(rankingLevelRequirementPatterns[i]);
//        }
//
//        CompetencyRankingPatternDetails[] competencyRankingPatternDetails = new CompetencyRankingPatternDetails[27];
//        competencyRankingPatternDetails[1] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[1],///change
//                rankingLevelRequirementPatterns[1]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[2] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[2],///change
//                rankingLevelRequirementPatterns[2]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails[3] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[3],///change
//                rankingLevelRequirementPatterns[3]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[4] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[4],///change
//                rankingLevelRequirementPatterns[4]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[5] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[5],///change
//                rankingLevelRequirementPatterns[5]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails[6] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[6],///change
//                rankingLevelRequirementPatterns[6]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[7] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[7],///change
//                rankingLevelRequirementPatterns[7]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[8] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[8],///change
//                rankingLevelRequirementPatterns[8]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails[9] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[9],///change
//                rankingLevelRequirementPatterns[9]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[10] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[10],///change
//                rankingLevelRequirementPatterns[10]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[11] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[11],///change
//                rankingLevelRequirementPatterns[11]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[12] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[12],///change
//                rankingLevelRequirementPatterns[12]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails[13] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[13],///change
//                rankingLevelRequirementPatterns[13]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[14] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[14],///change
//                rankingLevelRequirementPatterns[14]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[15] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[15],///change
//                rankingLevelRequirementPatterns[15]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[16] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[16],///change
//                rankingLevelRequirementPatterns[16]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[17] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[17],///change
//                rankingLevelRequirementPatterns[17]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails[18] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[18],///change
//                rankingLevelRequirementPatterns[18]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[19] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[19],///change
//                rankingLevelRequirementPatterns[19]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[20] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[20],///change
//                rankingLevelRequirementPatterns[20]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[21] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[21],///change
//                rankingLevelRequirementPatterns[21]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[22] = new CompetencyRankingPatternDetails(
//                (byte) 6, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[22],///change
//                rankingLevelRequirementPatterns[22]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[23] = new CompetencyRankingPatternDetails(
//                (byte) 6, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[23],///change
//                rankingLevelRequirementPatterns[23]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails[24] = new CompetencyRankingPatternDetails(
//                (byte) 5, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[24],///change
//                rankingLevelRequirementPatterns[24]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[25] = new CompetencyRankingPatternDetails(
//                (byte) 5, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[25],///change
//                rankingLevelRequirementPatterns[25]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails[26] = new CompetencyRankingPatternDetails(
//                (byte) 15, // total all 100%
//                competencyRankingPatterns1,
//                competencyComponentDetails[26],///change
//                rankingLevelRequirementPatterns[26]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        for (int i = 1; i < competencyRankingPatternDetails.length; i++) {
//            competencyRankingPatternDetailsRepository.save(competencyRankingPatternDetails[i]);
//        }
//
//        CompetencyRankingPatterns competencyRankingPatterns2 = new CompetencyRankingPatterns();
//        competencyRankingPatterns2.setJobRoles(jobRoles5);
//        competencyRankingPatterns2.setPeriodPattern(periodPattern2);
//        competencyRankingPatterns2.setCompetencyComponentsList(competencyComponentsList2);
//        competencyRankingPatterns2.setActiveStatus(ActiveStatus.Activated);
//        competencyRankingPatternsRepository.save(competencyRankingPatterns2);
//
//        RankingLevelRequirementPatterns[] rankingLevelRequirementPatterns2 = new RankingLevelRequirementPatterns[24];
//        rankingLevelRequirementPatterns2[1] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(1),
//                proficiencyLevelsRepository.findByProficiencyLevelId(7)
//        );
//        rankingLevelRequirementPatterns2[2] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(11),
//                proficiencyLevelsRepository.findByProficiencyLevelId(13)
//        );
//        rankingLevelRequirementPatterns2[3] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(14),
//                proficiencyLevelsRepository.findByProficiencyLevelId(19)
//        );
//        rankingLevelRequirementPatterns2[4] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(25),
//                proficiencyLevelsRepository.findByProficiencyLevelId(26)
//        );
//        rankingLevelRequirementPatterns2[5] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(29),
//                proficiencyLevelsRepository.findByProficiencyLevelId(32)
//        );
//        rankingLevelRequirementPatterns2[6] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(32),
//                proficiencyLevelsRepository.findByProficiencyLevelId(32)
//        );
//        rankingLevelRequirementPatterns2[7] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(33),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[8] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(34),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[9] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(35),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[10] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(36),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[11] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(37),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[12] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(38),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[13] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(37),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[14] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(37),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[15] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(35),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[16] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(33),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[17] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(32),
//                proficiencyLevelsRepository.findByProficiencyLevelId(32)
//        );
//        rankingLevelRequirementPatterns2[18] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(39),
//                proficiencyLevelsRepository.findByProficiencyLevelId(39)
//        );
//        rankingLevelRequirementPatterns2[19] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(40),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns2[20] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(41),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns2[21] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(42),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns2[22] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(43),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//        rankingLevelRequirementPatterns2[23] = new RankingLevelRequirementPatterns(
//                proficiencyLevelsRepository.findByProficiencyLevelId(44),
//                proficiencyLevelsRepository.findByProficiencyLevelId(46)
//        );
//
//        for (int i = 1; i < rankingLevelRequirementPatterns2.length; i++) {
//            rankingLevelRequirementPatternsRepository.save(rankingLevelRequirementPatterns2[i]);
//        }
//
//        CompetencyRankingPatternDetails competencyRankingPatternDetails2[] = new CompetencyRankingPatternDetails[24];
//        competencyRankingPatternDetails2[1] = new CompetencyRankingPatternDetails(
//                (byte) 8, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[1],///change
//                rankingLevelRequirementPatterns2[1]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[2] = new CompetencyRankingPatternDetails(
//                (byte) 8, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[2],///change
//                rankingLevelRequirementPatterns2[2]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails2[3] = new CompetencyRankingPatternDetails(
//                (byte) 8, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[3],///change
//                rankingLevelRequirementPatterns2[3]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[4] = new CompetencyRankingPatternDetails(
//                (byte) 8, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[4],///change
//                rankingLevelRequirementPatterns2[4]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[5] = new CompetencyRankingPatternDetails(
//                (byte) 8, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[5],///change
//                rankingLevelRequirementPatterns2[5]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails2[6] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[6],///change
//                rankingLevelRequirementPatterns2[6]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[7] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[7],///change
//                rankingLevelRequirementPatterns2[7]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[8] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[8],///change
//                rankingLevelRequirementPatterns2[8]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails2[9] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[9],///change
//                rankingLevelRequirementPatterns2[9]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[10] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[10],///change
//                rankingLevelRequirementPatterns2[10]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[11] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[11],///change
//                rankingLevelRequirementPatterns2[11]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[12] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[12],///change
//                rankingLevelRequirementPatterns2[12]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails2[13] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[13],///change
//                rankingLevelRequirementPatterns2[13]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[14] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[14],///change
//                rankingLevelRequirementPatterns2[14]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[15] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[15],///change
//                rankingLevelRequirementPatterns2[15]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[16] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[16],///change
//                rankingLevelRequirementPatterns2[16]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[17] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[17],///change
//                rankingLevelRequirementPatterns2[17]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        competencyRankingPatternDetails2[18] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[18],///change
//                rankingLevelRequirementPatterns2[18]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[19] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[19],///change
//                rankingLevelRequirementPatterns2[19]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[20] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[20],///change
//                rankingLevelRequirementPatterns2[20]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[21] = new CompetencyRankingPatternDetails(
//                (byte) 3, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[21],///change
//                rankingLevelRequirementPatterns2[21]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[22] = new CompetencyRankingPatternDetails(
//                (byte) 6, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[22],///change
//                rankingLevelRequirementPatterns2[22]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//        competencyRankingPatternDetails2[23] = new CompetencyRankingPatternDetails(
//                (byte) 6, // total all 100%
//                competencyRankingPatterns2,
//                competencyComponentDetails[23],///change
//                rankingLevelRequirementPatterns2[23]/// change base on bottom
//                , EvidenceTypes.Optional
//        );
//
//        for (int i = 1; i < competencyRankingPatternDetails2.length; i++) {
//            competencyRankingPatternDetailsRepository.save(competencyRankingPatternDetails2[i]);
//        }
    }

    private void saveDataSourceList(List<DataSources> dataSourcesList) {
        for (DataSources e : dataSourcesList) {
            dataSourceServiceImp.save(e);
        }
    }
}
