package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.ProficiencyLevels;

public interface ProficiencyLevelsService {

    ProficiencyLevels findProficiencyLevelsById(int id);

    ProficiencyLevels findFirstByProficiencyLevelTypes_ProficiencyLevelTypeIdAndDesc(short id);
}
