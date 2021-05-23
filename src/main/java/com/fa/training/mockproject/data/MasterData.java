package com.fa.training.mockproject.data;

import com.fa.training.mockproject.entities.ProficiencyLevelTypes;
import com.fa.training.mockproject.entities.ProficiencyLevels;
import com.fa.training.mockproject.repositories.ProficiencyLevelTypesRepository;
import com.fa.training.mockproject.repositories.ProficiencyLevelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class MasterData {

    @Autowired
    private ProficiencyLevelsRepository proficiencyLevelsRepository;

    @Autowired
    private ProficiencyLevelTypesRepository proficiencyLevelTypesRepository;

    public void insertMasterData() {

        List<ProficiencyLevelTypes> proficiencyLevelTypesList = new ArrayList<>();

        //Responsibilities and Roles
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Responsibilities and Duties"));
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Job Roles"));

        //Business Contributions
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Organization Contributions"));
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Project Contributions"));
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Personal Contributions"));

        //Professional [Software Engineering] Skills
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Frameworks and Runtime Engine, Development Tools and Environment, Programming Language, " +
                "Optional Professional, Mobile Skills, Cloud Computing Skills, Specialized Software and Tools, Optional Engineering, Business or Domain Skills, " +
                "[Specific] Functional & Application Skills, Product and Service Management Skills, Pre-Sales / Technical Sales Skills, Workplace Skills and Soft Skills"));

        //Natural Language Skills
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("English Skills, Japanese Skills, Chinese Skills, Korean Skills, French Skills, " +
                "Vietnamese Skills"));

        //Education and Knowledge
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Methodologies and Knowledge"));
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Education and Qualifications"));
        proficiencyLevelTypesList.add(new ProficiencyLevelTypes("Professional Certifications"));

        for (ProficiencyLevelTypes e : proficiencyLevelTypesList) {
            proficiencyLevelTypesRepository.save(e);
        }

        List<ProficiencyLevels> proficiencyLevelsList = new ArrayList<>();

        //Responsibilities and Duties
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No Responsibility", proficiencyLevelTypesList.get(0)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, "Follow", proficiencyLevelTypesList.get(0)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "Assist", proficiencyLevelTypesList.get(0)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "Apply", proficiencyLevelTypesList.get(0)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "Create", proficiencyLevelTypesList.get(0)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, "Design", proficiencyLevelTypesList.get(0)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 6, "Initiate", proficiencyLevelTypesList.get(0)));
        //Job Roles
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No Role", proficiencyLevelTypesList.get(1)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, "Very Low", proficiencyLevelTypesList.get(1)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "Low", proficiencyLevelTypesList.get(1)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "Medium", proficiencyLevelTypesList.get(1)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "High", proficiencyLevelTypesList.get(1)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, "Very High", proficiencyLevelTypesList.get(1)));
        //Organization Contributions
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No KPI/REC Point", proficiencyLevelTypesList.get(2)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, " 1-5 KPI/REC Point", proficiencyLevelTypesList.get(2)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "6-10 KPI/REC Point", proficiencyLevelTypesList.get(2)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "11-15 KPI/REC Point", proficiencyLevelTypesList.get(2)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "16-20 KPI/REC Point", proficiencyLevelTypesList.get(2)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, ">21 KPI/REC Poin  t", proficiencyLevelTypesList.get(2)));
        //Project Contributions
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No man-month", proficiencyLevelTypesList.get(3)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, " 1-5 man-month", proficiencyLevelTypesList.get(3)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "6-10 man-month", proficiencyLevelTypesList.get(3)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "11-15 man-month", proficiencyLevelTypesList.get(3)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "16-20 man-month", proficiencyLevelTypesList.get(3)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, "21-30 man-month", proficiencyLevelTypesList.get(3)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 6, ">30 man-month", proficiencyLevelTypesList.get(3)));
        //Personal Contributions
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No  Point", proficiencyLevelTypesList.get(4)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, " 1-5 Points", proficiencyLevelTypesList.get(4)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "6-10 Points", proficiencyLevelTypesList.get(4)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "11-15 Points", proficiencyLevelTypesList.get(4)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "16-20 Points", proficiencyLevelTypesList.get(4)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, ">21 Points", proficiencyLevelTypesList.get(4)));
        //page Professional Skills: Frameworks and Runtime Engine, Development Tools and Environment, Programming Language, Optional Professional
        //page EngineeringSkills: Mobile Skills, Cloud Computing Skills, Specialized Software and Tools, Optional Engineering
        //page NonEngineeringSkills, Business or Domain Skills, [Specific] Functional & Application Skills, Product and Service Management Skills,Pre-Sales / Technical Sales Skills
        //Workplace Skills and Soft Skills
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No Knowledge", proficiencyLevelTypesList.get(5)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, "Fundamental Awareness", proficiencyLevelTypesList.get(5)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "Limited Experience", proficiencyLevelTypesList.get(5)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "Intermediate", proficiencyLevelTypesList.get(5)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "Advanced", proficiencyLevelTypesList.get(5)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, "Expert", proficiencyLevelTypesList.get(5)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 6, "Master", proficiencyLevelTypesList.get(5)));

        //page NaturalLanguageSkills: English Skills, Japanese Skills, Chinese Skills, Korean Skills, French Skills, Vietnamese Skills
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No Knowledge", proficiencyLevelTypesList.get(6)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, "Elementary", proficiencyLevelTypesList.get(6)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "Limited Working", proficiencyLevelTypesList.get(6)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "General Professional", proficiencyLevelTypesList.get(6)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "Full Professional", proficiencyLevelTypesList.get(6)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, "Near Native", proficiencyLevelTypesList.get(6)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 6, "Native", proficiencyLevelTypesList.get(6)));

        //Methodologies and Knowledge
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No Knowledge", proficiencyLevelTypesList.get(7)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, "Fundamental Awareness", proficiencyLevelTypesList.get(7)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "Limited Experience", proficiencyLevelTypesList.get(7)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "Intermediate", proficiencyLevelTypesList.get(7)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "Advanced", proficiencyLevelTypesList.get(7)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, "Expert", proficiencyLevelTypesList.get(7)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 6, "Master", proficiencyLevelTypesList.get(7)));

        //Education and Qualifications
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No Degree", proficiencyLevelTypesList.get(8)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, "Post Master's Degree", proficiencyLevelTypesList.get(8)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "Master's Degree", proficiencyLevelTypesList.get(8)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "Bachelor's Degree", proficiencyLevelTypesList.get(8)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "Post-Baccalaureate", proficiencyLevelTypesList.get(8)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 5, "Associate of Business/ Science", proficiencyLevelTypesList.get(8)));

        //Professional Certifications
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 0, "No Level", proficiencyLevelTypesList.get(9)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 1, "Engineering Level 1", proficiencyLevelTypesList.get(9)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 2, "Engineering Level 2", proficiencyLevelTypesList.get(9)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 3, "Engineering Level 3", proficiencyLevelTypesList.get(9)));
        proficiencyLevelsList.add(new ProficiencyLevels((byte) 4, "Engineering Level 4", proficiencyLevelTypesList.get(9)));

        for (ProficiencyLevels e : proficiencyLevelsList) {
            proficiencyLevelsRepository.save(e);
        }
    }
}
