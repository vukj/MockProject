package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.RankingLevelRequirementPatterns;
import com.fa.training.mockproject.repositories.RankingLevelRequirementPatternsRepository;
import com.fa.training.mockproject.services.RankingLevelRequirementPatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RankingLevelRequirementPatternServiceImpl implements RankingLevelRequirementPatternService {

    @Autowired
    private RankingLevelRequirementPatternsRepository rankingLevelRequirementPatternsRepository;

    @Override
    public void save(RankingLevelRequirementPatterns rankingLevelRequirementPatterns) {
        rankingLevelRequirementPatternsRepository.save(rankingLevelRequirementPatterns);
    }

    @Override
    public RankingLevelRequirementPatterns findById(int id) {
        return rankingLevelRequirementPatternsRepository.findByRankingLevelRequirementPatternId(id);
    }
}
