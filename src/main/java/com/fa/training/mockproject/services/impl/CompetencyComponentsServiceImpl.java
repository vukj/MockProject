package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.CompetencyComponents;
import com.fa.training.mockproject.repositories.CompetencyComponentsRepository;
import com.fa.training.mockproject.services.CompetencyComponentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CompetencyComponentsServiceImpl implements CompetencyComponentsService {

    @Autowired
    private CompetencyComponentsRepository competencyComponentsRepository;

    @Override
    public List<CompetencyComponents> getAllCompetencyComponents() {
        return competencyComponentsRepository.findAll();
    }

    @Override
    public CompetencyComponents findByComponentName(String componentName) {
        return competencyComponentsRepository.findDistinctByComponentName(componentName);
    }

    @Override
    public CompetencyComponents findById(int id) {
        return competencyComponentsRepository.findByComponentId(id);
    }


}
