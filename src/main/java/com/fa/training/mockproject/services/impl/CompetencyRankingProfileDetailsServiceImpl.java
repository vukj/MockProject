package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.CompetencyRankingProfileDetails;
import com.fa.training.mockproject.repositories.CompetencyRankingProfilesDetailsRepository;
import com.fa.training.mockproject.services.CompetencyRankingProfileDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CompetencyRankingProfileDetailsServiceImpl implements CompetencyRankingProfileDetailsService {

    @Autowired
    private CompetencyRankingProfilesDetailsRepository competencyRankingProfilesDetailsRepository;

    public void save(CompetencyRankingProfileDetails profileDetails) {
        competencyRankingProfilesDetailsRepository.save(profileDetails);
    }

    @Override
    public List<CompetencyRankingProfileDetails> findAllByCompetencyRankingPatternDetails_CompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingProfiles_RankingProfileId(int componentId, int profileId) {
        return competencyRankingProfilesDetailsRepository.findAllByCompetencyRankingPatternDetails_CompetencyComponentDetails_CompetencyComponents_ComponentIdAndCompetencyRankingProfiles_RankingProfileId(componentId, profileId);
    }
}
