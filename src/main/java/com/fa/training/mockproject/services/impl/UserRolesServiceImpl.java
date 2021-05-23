package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.UserRoles;
import com.fa.training.mockproject.repositories.UserRolesRepository;
import com.fa.training.mockproject.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserRolesServiceImpl implements UserRoleService {

    @Autowired
    private UserRolesRepository userRolesRepository;

    /**
     * Save User Role
     *
     * @param userRoles
     */
    @Override
    public void saveRole(UserRoles userRoles) {
        userRolesRepository.save(userRoles);
    }

    /**
     * Get User Role by name
     *
     * @param role
     * @return
     */
    @Override
    public UserRoles getUserRoles(String role) {
        return userRolesRepository.findByUserRoleName(role);
    }
}
