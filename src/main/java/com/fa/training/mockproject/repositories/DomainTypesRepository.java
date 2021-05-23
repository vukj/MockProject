package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.DomainTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainTypesRepository extends JpaRepository<DomainTypes, Short> {

    DomainTypes findByDomainTypeId(Short id);
}
