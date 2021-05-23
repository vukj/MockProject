package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.JobRoles;
import com.fa.training.mockproject.repositories.JobRolesRepository;
import com.fa.training.mockproject.services.JobRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class JobRolesServiceImpl implements JobRolesService {

    @Autowired
    private JobRolesRepository jobRolesRepository;

    @Override
    public JobRoles findByJobRoleName(String name) {
        return jobRolesRepository.findByJobRoleName(name);
    }

    @Override
    public void saveJobRoles(JobRoles jobRoles) {
        jobRolesRepository.save(jobRoles);
    }

    @Override
    public JobRoles findById(int id) {
        return jobRolesRepository.findByJobRoleId(id);
    }

    @Override
    public List<JobRoles> getAll() {
        return jobRolesRepository.findAll();
    }

    @Override
    public JobRoles findByDomains_DomainIdAndJobRoleName(int id, String name) {
        return jobRolesRepository.findByDomains_DomainIdAndJobRoleName(id, name);
    }

    @Override
    public List<JobRoles> findAllByDomains_DomainId(int id) {
        return jobRolesRepository.findAllByDomains_DomainId(id);
    }

    @Override
    public List<JobRoles> findAllDomainNameGBName() {
        return jobRolesRepository.findAllJobRoleGroupByName();
    }

}
