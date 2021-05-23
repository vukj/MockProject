package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.CompetencyComponentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetencyComponentDetailsRepository extends JpaRepository<CompetencyComponentDetails, Integer> {

    CompetencyComponentDetails findCompetencyComponentDetailsByComponentDetailName(String name);

    CompetencyComponentDetails findCompetencyComponentDetailsByComponentDetailId(int id);

    CompetencyComponentDetails findByComponentDetailId(int id);
}
