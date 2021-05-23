package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.CompetencyComponents;

import java.util.List;

public interface CompetencyComponentsService {

    List<CompetencyComponents> getAllCompetencyComponents();

    CompetencyComponents findByComponentName(String componentName);

    CompetencyComponents findById(int id);

}
