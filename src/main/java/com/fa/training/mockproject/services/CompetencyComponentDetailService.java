package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.CompetencyComponentDetails;

public interface CompetencyComponentDetailService {

    CompetencyComponentDetails findCompetencyComponentDetailsByNames(String name);

    CompetencyComponentDetails findCompetencyComponentDetailsById(int id);

}
