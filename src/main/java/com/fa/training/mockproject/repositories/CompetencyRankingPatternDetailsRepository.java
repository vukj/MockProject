package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.CompetencyRankingPatternDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetencyRankingPatternDetailsRepository extends JpaRepository<CompetencyRankingPatternDetails, Integer> {

    CompetencyRankingPatternDetails findByCompetencyPatternDetailId(int id);

    List<CompetencyRankingPatternDetails> findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(int componentId, int patternId);

    List<CompetencyRankingPatternDetails> findAllByCompetencyRankingPatterns_CompetencyPatternId(int id);

    CompetencyRankingPatternDetails findByCompetencyComponentDetails_ComponentDetailId(int id);

    CompetencyRankingPatternDetails findCompetencyRankingPatternDetailsByCompetencyComponentDetails_ComponentDetailIdAndCompetencyRankingPatterns_CompetencyPatternId(int componentDetailId, int competencyPatternId);
}
