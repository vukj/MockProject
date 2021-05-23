package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.DomainTypes;
import com.fa.training.mockproject.repositories.DomainTypesRepository;
import com.fa.training.mockproject.services.DomainTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DomainTypeServiceImpl implements DomainTypeService {

    @Autowired
    private DomainTypesRepository domainTypesRepository;

    @Override
    public void saveDomainType(DomainTypes domainTypes) {
        domainTypesRepository.save(domainTypes);
    }

    @Override
    public List<DomainTypes> getAllDomainType() {
        return domainTypesRepository.findAll();
    }

    @Override
    public void deleteDomainType(DomainTypes domainTypes) {
        domainTypesRepository.delete(domainTypes);
    }

    @Override
    public DomainTypes findDomainTypeById(short id) {
        return domainTypesRepository.findByDomainTypeId(id);
    }
}
