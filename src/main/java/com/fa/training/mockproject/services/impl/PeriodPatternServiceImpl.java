package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.PeriodPattern;
import com.fa.training.mockproject.repositories.PeriodPatternRepository;
import com.fa.training.mockproject.services.PeriodPatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PeriodPatternServiceImpl implements PeriodPatternService {

    @Autowired
    private PeriodPatternRepository periodPatternRepository;

    /**
     * Save an Period Pattern
     *
     * @param periodPattern
     */
    @Override
    public void savePeriodPattern(PeriodPattern periodPattern) {
        periodPatternRepository.save(periodPattern);
    }

    /**
     * Get list Period Pattern
     *
     * @return
     */
    @Override
    public List<PeriodPattern> getALlListPeriodPattern() {
        return periodPatternRepository.findAll();
    }

    /**
     * Get Period Pattern by ID
     *
     * @param id
     * @return
     */
    @Override
    public PeriodPattern getPeriodPatternById(Long id) {
        return periodPatternRepository.findByPeriodId(id);
    }

    @Override
    public List<PeriodPattern> getAllPeriodByName(String name) {
        return periodPatternRepository.getAllByName(name);
    }

    @Override
    public List<PeriodPattern> findAllByOrderByYearAscNameAsc() {
        return periodPatternRepository.findAllByOrderByYearAscNameAsc();
    }
}
