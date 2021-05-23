package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.ProficiencyLevelTypes;
import com.fa.training.mockproject.entities.ProficiencyLevels;
import com.fa.training.mockproject.repositories.ProficiencyLevelsRepository;
import com.fa.training.mockproject.services.ProficiencyLevelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProficiencyLevelsServiceImpl implements ProficiencyLevelsService {

    @Autowired
    private ProficiencyLevelsRepository proficiencyLevelsRepository;

    public ProficiencyLevels findByLevelTypeAndProficiencyLevels(ProficiencyLevelTypes proficiencyLevelTypes, byte proficiencyLevel) {
        return proficiencyLevelsRepository.findByProficiencyLevelTypesAndProficiencyLevel(proficiencyLevelTypes, proficiencyLevel);
    }

    @Override
    public ProficiencyLevels findProficiencyLevelsById(int id) {
        return proficiencyLevelsRepository.findByProficiencyLevelId(id);
    }

    @Override
    public ProficiencyLevels findFirstByProficiencyLevelTypes_ProficiencyLevelTypeIdAndDesc(short id) {
        return proficiencyLevelsRepository.findFirstByProficiencyLevelTypes_ProficiencyLevelTypeIdOrderByProficiencyLevelDesc(id);
    }
}
