package com.fa.training.mockproject.repositories;


import com.fa.training.mockproject.entities.PeriodPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PeriodPatternRepository extends JpaRepository<PeriodPattern, Long> {
    PeriodPattern findByPeriodId(long id);

    List<PeriodPattern> getAllByName(String name);

    List<PeriodPattern> findAllByOrderByYearAsc();

    List<PeriodPattern> findAllByOrderByYearAscNameAsc();
}
