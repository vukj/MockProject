package com.fa.training.mockproject.services.mapper;

import com.fa.training.mockproject.entities.CommonGenericClass;
import com.fa.training.mockproject.entities.CompetencyRankingPatterns;
import com.fa.training.mockproject.entities.dto.PatternDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// Tan code
@Mapper(uses = {PatternMapper.class, CommonGenericClass.class})
public interface PatternMapper {
    PatternMapper INSTANCE = Mappers.getMapper(PatternMapper.class);

    PatternDTO toDTO(CompetencyRankingPatterns competencyRankingPatterns);

    CompetencyRankingPatterns fromDTO(PatternDTO patternDTO);
}
