package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.CompetencyComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetencyComponentsRepository extends JpaRepository<CompetencyComponents, Integer> {

    CompetencyComponents findByComponentId(int id);

    CompetencyComponents findDistinctByComponentName(String componentName);

}
