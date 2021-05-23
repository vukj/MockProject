package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.JobRoles;

import java.util.List;

public interface JobRolesService {
    JobRoles findByJobRoleName(String name);

    void saveJobRoles(JobRoles jobRoles);

    JobRoles findById(int id);

    List<JobRoles> getAll();

    JobRoles findByDomains_DomainIdAndJobRoleName(int id, String name);

    List<JobRoles> findAllByDomains_DomainId(int id);

    public List<JobRoles> findAllDomainNameGBName();
}
