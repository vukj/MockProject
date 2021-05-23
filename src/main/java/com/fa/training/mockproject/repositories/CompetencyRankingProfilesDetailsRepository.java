package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.CompetencyRankingProfileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface
CompetencyRankingProfilesDetailsRepository extends JpaRepository<CompetencyRankingProfileDetails, Integer> {

    List<CompetencyRankingProfileDetails> findAllByCompetencyRankingPatternDetails_CompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingProfiles_RankingProfileId(int componentId, int profileId);
}
