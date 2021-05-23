package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.ProficiencyLevelTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProficiencyLevelTypesRepository extends JpaRepository<ProficiencyLevelTypes, Short> {
    ProficiencyLevelTypes findByProficiencyLevelTypeId(short proficiencyLevelTypeId);
}
