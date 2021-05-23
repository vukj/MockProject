package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CompetencyResults;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewPageInfoDTO {
    private List<CRProfileDetailAndReviewLevelDTO> crProfileDetailAndReviewLevelList;
    private int totalWeight;
    private CompetencyResults competencyResults;
    private List<Integer> listIndexUnreviewedCComponent;
}
