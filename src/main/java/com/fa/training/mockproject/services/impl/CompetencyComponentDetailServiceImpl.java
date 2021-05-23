package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.CompetencyComponentDetails;
import com.fa.training.mockproject.repositories.CompetencyComponentDetailsRepository;
import com.fa.training.mockproject.services.CompetencyComponentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CompetencyComponentDetailServiceImpl implements CompetencyComponentDetailService {

    @Autowired
    private CompetencyComponentDetailsRepository competencyComponentDetailsRepository;

    @Override
    public CompetencyComponentDetails findCompetencyComponentDetailsByNames(String name) {
        return competencyComponentDetailsRepository.findCompetencyComponentDetailsByComponentDetailName(name);
    }

    @Override
    public CompetencyComponentDetails findCompetencyComponentDetailsById(int id) {
        return competencyComponentDetailsRepository.findCompetencyComponentDetailsByComponentDetailId(id);
    }
}
