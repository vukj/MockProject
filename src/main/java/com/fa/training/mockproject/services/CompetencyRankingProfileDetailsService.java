package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.CompetencyRankingProfileDetails;

import java.util.List;

public interface CompetencyRankingProfileDetailsService {

    void save(CompetencyRankingProfileDetails competencyRankingProfileDetails);

    List<CompetencyRankingProfileDetails> findAllByCompetencyRankingPatternDetails_CompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingProfiles_RankingProfileId(int componentId, int profileId);
}
