package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.Domains;

import java.util.List;

public interface DomainService {

    void saveDomain(Domains domains);

    List<Domains> getAllDomains();

    void deleteDomain(Domains domains);

    Domains findDomainById(int id);

    void updateDomain(Domains domains);

    Domains findDomainByDomainName(String name);


}
