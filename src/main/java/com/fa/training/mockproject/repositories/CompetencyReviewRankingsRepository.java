package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.CompetencyReviewRankings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetencyReviewRankingsRepository extends JpaRepository<CompetencyReviewRankings, Integer> {
}
