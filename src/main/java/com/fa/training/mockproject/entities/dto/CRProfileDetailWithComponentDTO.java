package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyComponents;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CRProfileDetailWithComponentDTO {
    private CompetencyComponents competencyComponents;
    private List<CRProfileDetailAndReviewLevelDTO> crProfileDetailAndReviewLevelList;
    private float totalWeight;
}
