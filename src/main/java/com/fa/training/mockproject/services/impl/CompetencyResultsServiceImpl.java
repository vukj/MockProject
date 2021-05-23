package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.CompetencyResults;
import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.repositories.CompetencyResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CompetencyResultsServiceImpl {

    @Autowired
    private CompetencyResultsRepository competencyResultsRepository;

    public CompetencyResults findByCompetencyRankingProfileAndReviewer(CompetencyRankingProfiles competencyRankingProfiles, Employees reviewer) {
        return competencyResultsRepository.findByCompetencyRankingProfileAndReviewer(competencyRankingProfiles, reviewer);
    }

    public void save(CompetencyResults competencyResults) {
        competencyResultsRepository.save(competencyResults);
    }
}
