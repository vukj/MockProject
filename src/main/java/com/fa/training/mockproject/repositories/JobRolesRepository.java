package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.JobRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRolesRepository extends JpaRepository<JobRoles, Integer> {

    JobRoles findByJobRoleId(int id);

    JobRoles findByJobRoleName(String JobRoleName);

    JobRoles findByDomains_DomainIdAndJobRoleName(int id, String name);

    List<JobRoles> findAllByDomains_DomainId(int id);

    @Query("FROM JobRoles j group by j.jobRoleName")
    List<JobRoles> findAllJobRoleGroupByName();
}
