package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.RankingLevelRequirementPatterns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingLevelRequirementPatternsRepository extends JpaRepository<RankingLevelRequirementPatterns, Integer> {

    RankingLevelRequirementPatterns findByRankingLevelRequirementPatternId(int id);
}
