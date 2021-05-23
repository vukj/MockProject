package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.UserAccountDetails;
import com.fa.training.mockproject.entities.UserAccounts;
import com.fa.training.mockproject.entities.UserRoles;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.repositories.UserAccountsRepository;
import com.fa.training.mockproject.repositories.UserRolesRepository;
import com.fa.training.mockproject.services.DomainService;
import com.fa.training.mockproject.services.UserAccountsService;
import com.fa.training.mockproject.services.mapper.UserAccountsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserAccountsServiceImpl implements UserAccountsService, UserDetailsService {

    @Autowired
    private UserAccountsRepository userAccountsRepository;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private DomainService domainService;


    /**
     * Update an avatar account
     *
     * @param userAccountsLoginDTO
     */
    @Override
    public void updateAvatar(UserAccountsLoginDTO userAccountsLoginDTO) {
        UserAccounts userAccounts = userAccountsRepository.findByEmail(userAccountsLoginDTO.getEmail());
        userAccounts.getEmployees().setAvatar(userAccountsLoginDTO.getEmployees().getAvatar());
        userAccountsRepository.save(userAccounts);
    }

    /**
     * update last loged in
     *
     * @param userAccountsLoginDTO
     */
    @Override
    public void updateLastLogged(UserAccountsLoginDTO userAccountsLoginDTO) {
        UserAccounts userAccounts = userAccountsRepository.findByEmail(userAccountsLoginDTO.getEmail());
        userAccounts.setLastLogged(new Date());
        userAccountsRepository.save(userAccounts);
    }

    /**
     * Save a new account or update password
     *
     * @param userAccountsLoginDTO
     */
    @Override
    public void saveAccount(UserAccountsLoginDTO userAccountsLoginDTO) {
        if (!userAccountsRepository.existsByEmail(userAccountsLoginDTO.getEmail())) {
            UserAccounts userAccounts = new UserAccounts();
            userAccounts.setEmployees(userAccountsLoginDTO.getEmployees());
            userAccounts.setEmail(userAccountsLoginDTO.getEmail());
            userAccounts.setActiveStatus(ActiveStatus.Inactive);
            userAccounts.setUserRoles(userRolesRepository.findByUserRoleName("ROLE_STAFF"));
            userAccountsLoginDTO.getEmployees().setDomains(domainService.findDomainByDomainName("New Account"));
            userAccountsRepository.save(userAccounts);
        } else {
            UserAccounts userAccounts = userAccountsRepository.findByEmail(userAccountsLoginDTO.getEmail());
            userAccounts.setPassword(userAccountsLoginDTO.getPassword());
            userAccounts.setActiveStatus(ActiveStatus.Activated);
            userAccountsRepository.save(userAccounts);
        }
    }

    /**
     * Deactivated an account
     *
     * @param userAccountsLoginDTO
     */
    @Override
    public void deactiveAccount(UserAccountsLoginDTO userAccountsLoginDTO) {
        UserAccounts userAccounts = userAccountsRepository.findByEmail(userAccountsLoginDTO.getEmail());
        userAccounts.setActiveStatus(ActiveStatus.Inactive);
        userAccountsRepository.save(userAccounts);
    }

    /**
     * Check email exits
     *
     * @param email
     * @return
     */
    @Override
    public boolean exitsByEmail(String email) {
        if (userAccountsRepository.existsByEmail(email)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find an user by Email
     *
     * @param email
     * @return
     */
    @Override
    public UserAccountsLoginDTO findByEmail(String email) {
        UserAccountsLoginDTO userAccountsLoginDTO = UserAccountsMapper.INSTANCE.toDTO(userAccountsRepository.findByEmail(email));
        return userAccountsLoginDTO;
    }

    /**
     * Security load account to verified
     *
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public List<UserAccounts> findAllUserAccounts() {
        return userAccountsRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccounts userAccounts = userAccountsRepository.findByEmail(email);
        UserRoles userRoles = null;
        if (userAccounts == null) {
            return null;
        }
        try {
            userRoles = userRolesRepository.findByUserRoleId(userAccounts.getUserRoles().getUserRoleId());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (userRoles != null) {
            GrantedAuthority authority = new SimpleGrantedAuthority(userRoles.getUserRoleName());
            authorities.add(authority);
        }

        UserAccountDetails userAccountDetails = new UserAccountDetails(email, userAccounts.getPassword(), authorities);
        return userAccountDetails;
    }
}

