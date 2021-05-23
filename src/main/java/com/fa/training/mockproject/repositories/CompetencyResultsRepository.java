package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.CompetencyRankingProfiles;
import com.fa.training.mockproject.entities.CompetencyResults;
import com.fa.training.mockproject.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetencyResultsRepository extends JpaRepository<CompetencyResults, Integer> {
    CompetencyResults findByCompetencyRankingProfileAndReviewer(CompetencyRankingProfiles competencyRankingProfiles, Employees reviewer);
}
