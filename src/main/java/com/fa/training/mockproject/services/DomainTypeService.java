package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.DomainTypes;

import java.util.List;

public interface DomainTypeService {

    void saveDomainType(DomainTypes domainTypes);

    List<DomainTypes> getAllDomainType();

    void deleteDomainType(DomainTypes domainTypes);

    DomainTypes findDomainTypeById(short id);
}
