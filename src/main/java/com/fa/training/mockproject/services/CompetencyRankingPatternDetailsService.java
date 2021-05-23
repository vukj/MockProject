package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.CompetencyRankingPatternDetails;
import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.entities.dto.PatternSummaryDTO;

import java.util.List;

public interface CompetencyRankingPatternDetailsService {

    void save(CompetencyRankingPatternDetails competencyRankingPatternDetails);

    CompetencyRankingPatternDetails findById(int id);

    List<CompetencyRankingPatternDetails> findAllByCompetencyRankingPatternId(int id);

    void update(CompetencyRankingPatternDetails competencyRankingPatternDetails);

    CompetencyRankingPatternDetails findByCompetencyComponentDetailsId(int id);

    CompetencyRankingPatternDetails findByComponentDetailsIdAndCompetencyRankingPatternId(int componentDetailsId, int competencyRankingPatternId);

    List<CompetencyRankingPatternDetails> findAllByCompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingPatterns_CompetencyPatternId(int componentId, int patternId);

    void delete(CompetencyRankingPatternDetails competencyRankingPatternDetails);

    List<PatternSummaryDTO> calculateAndShowInformationForAllComponent(CompetencyRankingPatterns competencyRankingPatterns);
}
