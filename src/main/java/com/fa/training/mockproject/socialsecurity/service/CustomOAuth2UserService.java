package com.fa.training.mockproject.socialsecurity.service;

import com.fa.training.mockproject.entities.*;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.repositories.UserAccountsRepository;
import com.fa.training.mockproject.repositories.UserRolesRepository;
import com.fa.training.mockproject.services.*;
import com.fa.training.mockproject.socialsecurity.model.CustomOAuth2User;
import com.fa.training.mockproject.socialsecurity.user.UserInfo;
import com.fa.training.mockproject.socialsecurity.user.UserInfoFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

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
     * Support login facebook
     *
     * @param customOAuth2User
     * @param oAuth2UserRequest
     * @return
     * @throws MessagingException
     */
    public OAuth2User processOAuth2User(CustomOAuth2User customOAuth2User, OAuth2UserRequest oAuth2UserRequest) throws MessagingException {
        // Get information by UserInfoFactory
        UserInfo oAuth2UserInfo = UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), customOAuth2User.getAttributes());
        // Find user by email if user is exist --> update // else --> register and send email to create password
        if (oAuth2UserInfo.getEmail() != null) {
            System.out.println(oAuth2UserInfo.getEmail());
            if (userAccountsService.findByEmail(oAuth2UserInfo.getEmail()) == null) {
                UserAccountsLoginDTO userAccountsLoginDTO = new UserAccountsLoginDTO();
                userAccountsLoginDTO.setEmail(oAuth2UserInfo.getEmail());
                // Create new employee base on information of userAccounts
                Employees employees = new Employees();
                employees.setFullName(oAuth2UserInfo.getName());
                employees.setAccount("fb_" + oAuth2UserInfo.getEmail().split("@")[0]);
                userAccountsLoginDTO.setEmployees(employees);
                userAccountsService.saveAccount(userAccountsLoginDTO);
                //  Send Email
                ConfirmationToken confirmationToken = new ConfirmationToken(oAuth2UserInfo.getEmail());
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
            String userFullNameFromOAuth2User = oAuth2UserInfo.getName();
            Employees employees = employeeService.findEmployeeByUserAccountId(userAccountsService.findByEmail(oAuth2UserInfo.getEmail()).getId());
            employees.setFullName(userFullNameFromOAuth2User);
            // Update information for employee
            employeeService.saveEmployee(employees);
        }
        // Find userAccountsLoginDTO by email
        UserAccountsLoginDTO userAccountsLoginDTO = userAccountsService.findByEmail(oAuth2UserInfo.getEmail());
        // Find userAccounts by email
        UserAccounts userAccounts = userAccountsRepository.findByEmail(oAuth2UserInfo.getEmail());
        // Add authorities by get role from UserAccounts
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserRoles userRoles = userRolesRepository.findByUserRoleId(userAccounts.getUserRoles().getUserRoleId());
        GrantedAuthority authority = new SimpleGrantedAuthority(userRoles.getUserRoleName());
        authorities.add(authority);
        // Create UserAccountDetails --> (1)
        UserAccountDetails userAccountDetails = new UserAccountDetails(userAccountsLoginDTO.getId(), userAccountsLoginDTO.getEmail(), userAccountsLoginDTO.getPassword(), authorities);
        userAccountDetails.setAttributes(customOAuth2User.getAttributes());
        //
        return userAccountDetails;
    }

    @SneakyThrows
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        // call processOAuth2User above
        return processOAuth2User(new CustomOAuth2User(oAuth2User), oAuth2UserRequest);
    }

}
