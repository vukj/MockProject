package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.ProficiencyLevelTypes;
import com.fa.training.mockproject.entities.ProficiencyLevels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProficiencyLevelsRepository extends JpaRepository<ProficiencyLevels, Integer> {
    ProficiencyLevels findFirstByProficiencyLevelName(String proficiencyLevel);

    ProficiencyLevels findByProficiencyLevelId(int id);

    ProficiencyLevels findFirstByProficiencyLevelTypes_ProficiencyLevelTypeIdOrderByProficiencyLevelDesc(short id);

    ProficiencyLevels findByProficiencyLevelTypesAndProficiencyLevel(ProficiencyLevelTypes proficiencyLevelTypes, byte proficiencyLevel);
}
