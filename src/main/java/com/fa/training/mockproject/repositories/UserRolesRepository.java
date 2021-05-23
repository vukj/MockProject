package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Integer> {

    UserRoles findByUserRoleId(int id);

    UserRoles findByUserRoleName(String roleName);
}
