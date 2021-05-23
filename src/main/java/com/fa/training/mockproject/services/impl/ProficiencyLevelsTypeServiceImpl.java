package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.ProficiencyLevelTypes;
import com.fa.training.mockproject.repositories.ProficiencyLevelTypesRepository;
import com.fa.training.mockproject.services.ProficiencyLevelsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProficiencyLevelsTypeServiceImpl implements ProficiencyLevelsTypeService {

    @Autowired
    ProficiencyLevelTypesRepository proficiencyLevelTypesRepository;

    /**
     * Save a ProficiencyLevel Types
     *
     * @param proficiencyLevelTypes
     */
    public void save(ProficiencyLevelTypes proficiencyLevelTypes) {
        proficiencyLevelTypesRepository.save(proficiencyLevelTypes);
    }

    @Override
    public ProficiencyLevelTypes findProficiencyLevelTypesById(short id) {
        return proficiencyLevelTypesRepository.findByProficiencyLevelTypeId(id);
    }
}
