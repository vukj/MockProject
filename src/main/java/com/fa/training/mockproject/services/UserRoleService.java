package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.UserRoles;

public interface UserRoleService {

    void saveRole(UserRoles userRoles);

    UserRoles getUserRoles(String role);

}
