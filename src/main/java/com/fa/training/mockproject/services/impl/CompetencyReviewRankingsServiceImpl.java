package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.CompetencyReviewRankings;
import com.fa.training.mockproject.repositories.CompetencyReviewRankingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompetencyReviewRankingsServiceImpl {

    @Autowired
    private CompetencyReviewRankingsRepository competencyReviewRankingsRepository;

    public void save(CompetencyReviewRankings competencyReviewRankings) {
        competencyReviewRankingsRepository.save(competencyReviewRankings);
    }
}
