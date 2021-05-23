package com.fa.training.mockproject.services.mapper;

import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileDTO toDTO(CompetencyRankingProfiles competencyRankingProfiles);

    CompetencyRankingProfiles fromDTO(ProfileDTO profileDTO);
}
