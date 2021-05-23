package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.ProficiencyLevelTypes;

public interface ProficiencyLevelsTypeService {
    void save(ProficiencyLevelTypes proficiencyLevelTypes);

    ProficiencyLevelTypes findProficiencyLevelTypesById(short id);
}
