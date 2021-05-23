package com.fa.training.mockproject.entities.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewLevelDTO {
    int sttOfCompetencyComponent;
    int idProfile;
    private List<Integer> levelReviewList;
}
