package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.RankingLevelRequirementPatterns;

public interface RankingLevelRequirementPatternService {

    void save(RankingLevelRequirementPatterns rankingLevelRequirementPatterns);

    RankingLevelRequirementPatterns findById(int id);

}
