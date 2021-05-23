package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.Domains;
import com.fa.training.mockproject.repositories.DomainsRepository;
import com.fa.training.mockproject.services.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class DomainServiceImpl implements DomainService {

    @Autowired
    private DomainsRepository domainsRepository;

    /**
     * Save a Domains
     *
     * @param domains
     */
    @Override
    public void saveDomain(Domains domains) {
        domainsRepository.save(domains);
    }

    /**
     * Get list Domains
     *
     * @return
     */
    @Override
    public List<Domains> getAllDomains() {
        return domainsRepository.findAll();
    }

    /**
     * delete a Domains
     *
     * @param domains
     */
    @Override
    public void deleteDomain(Domains domains) {
        domainsRepository.delete(domains);
    }

    /**
     * Find a Domains by ID
     *
     * @param id
     * @return
     */
    @Override
    public Domains findDomainById(int id) {
        return domainsRepository.findDomainsByDomainId(id);
    }

    @Override
    public void updateDomain(Domains domains) {
        domainsRepository.save(domains);
    }

    @Override
    public Domains findDomainByDomainName(String name) {
        Domains domains = new Domains();
        try {
            domains = domainsRepository.findDomainsByDomainName(name);
        } catch (Exception e) {
            domains = null;
            e.printStackTrace();
        }
        return domains;
    }
}
