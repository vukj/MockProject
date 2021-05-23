package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.Domains;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainsRepository extends JpaRepository<Domains, Integer> {

    Domains findDomainsByDomainId(int id);

    Domains findDomainsByDomainName(String name);
}
