package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.PeriodPattern;

import java.util.List;

public interface PeriodPatternService {

    void savePeriodPattern(PeriodPattern periodPattern);

    List<PeriodPattern> getALlListPeriodPattern();

    PeriodPattern getPeriodPatternById(Long id);

    List<PeriodPattern> getAllPeriodByName(String name);

    List<PeriodPattern> findAllByOrderByYearAscNameAsc();

}
