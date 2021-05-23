package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.UserAccounts;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserAccountsService extends UserDetailsService {

    void updateAvatar(UserAccountsLoginDTO userAccountsLoginDTO);

    void updateLastLogged(UserAccountsLoginDTO userAccountsLoginDTO);

    void saveAccount(UserAccountsLoginDTO userAccountsLoginDTO);

    void deactiveAccount(UserAccountsLoginDTO userAccountsLoginDTO);

    boolean exitsByEmail(String email);

    UserAccountsLoginDTO findByEmail(String email);

    List<UserAccounts> findAllUserAccounts();

}
