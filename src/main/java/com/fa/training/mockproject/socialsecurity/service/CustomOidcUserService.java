package com.fa.training.mockproject.socialsecurity.service;

import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.repositories.UserAccountsRepository;
import com.fa.training.mockproject.repositories.UserRolesRepository;
import com.fa.training.mockproject.services.*;
import com.fa.training.mockproject.socialsecurity.model.CustomOidcUser;
import com.fa.training.mockproject.socialsecurity.user.UserInfo;
import com.fa.training.mockproject.socialsecurity.user.UserInfoFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserAccountsService userAccountsService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private UserAccountsRepository userAccountsRepository;

    /**
     * Support login google
     *
     * @param customOidcUser
     * @param oidcUserRequest
     * @return
     * @throws MessagingException
     */
    public OidcUser processOidcUser(CustomOidcUser customOidcUser, OidcUserRequest oidcUserRequest) throws MessagingException {
        // Get information by UserInfoFactory
        UserInfo userInfo = UserInfoFactory.getOAuth2UserInfo(oidcUserRequest.getClientRegistration().getRegistrationId(), customOidcUser.getAttributes());
        // Find user by email if user is exist --> update else --> register and send email to create password
        if (userInfo.getEmail() != null) {
            if (!userAccountsService.exitsByEmail(userInfo.getEmail())) {
                UserAccountsLoginDTO userAccountsLoginDTO = new UserAccountsLoginDTO();
                userAccountsLoginDTO.setEmail(userInfo.getEmail());
                // Create new employee base on information of userAccounts
                Employees employees = new Employees();
                employees.setFullName(userInfo.getName());
                employees.setAccount("gg_" + userInfo.getEmail().split("@")[0]);
                userAccountsLoginDTO.setEmployees(employees);
                userAccountsService.saveAccount(userAccountsLoginDTO);
                ConfirmationToken confirmationToken = new ConfirmationToken(userInfo.getEmail());
                confirmationTokenService.save(confirmationToken);

                EmailTemplate emailTemplate = new EmailTemplate();
                String link = "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken();
                String content = "Dear " + userAccountsLoginDTO.getEmployees().getFullName() + ",<br>"
                        + "Please click the link below to verify your registration:<br>"
                        + "<h3><a href=" + link + ">VERIFY</a></h3>"
                        + "Thank you,<br>";
                emailTemplate.setFromAddress("fpt.academy@gmail.com");
                emailTemplate.setToAddress(userAccountsLoginDTO.getEmail());
                emailTemplate.setSubject("Verify your registration!");
                emailTemplate.setContent(content);
                emailSenderService.sendEmail(emailTemplate);
            }
        } else {
            String userFullNameFromOAuth2User = userInfo.getName();
            Employees employees = employeeService.findEmployeeByUserAccountId(userAccountsService.findByEmail(userInfo.getEmail()).getId());
            employees.setFullName(userFullNameFromOAuth2User);
            // Update information for employee
            employeeService.saveEmployee(employees);
        }
        // Find userAccountsLoginDTO by email
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(userInfo.getEmail());
        // Find userAccounts by email
        UserAccounts userAccounts = userAccountsRepository.findByEmail(userInfo.getEmail());
        // Add authorities by get role from UserAccounts
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserRoles userRoles = userRolesRepository.findByUserRoleId(userAccounts.getUserRoles().getUserRoleId());
        GrantedAuthority authority = new SimpleGrantedAuthority(userRoles.getUserRoleName());
        authorities.add(authority);
        // Create UserAccountDetails --> (1)
        UserAccountDetails userAccountDetails = new UserAccountDetails(userAccountsLoginDTO.getId(), userAccountsLoginDTO.getEmail(), userAccountsLoginDTO.getPassword(), authorities);
        userAccountDetails.setAttributes(customOidcUser.getAttributes());
        //
        return userAccountDetails;
    }

    @SneakyThrows
    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(oidcUserRequest);
        // call processOidcUser above
        return processOidcUser(new CustomOidcUser(oidcUser), oidcUserRequest);
    }
}
