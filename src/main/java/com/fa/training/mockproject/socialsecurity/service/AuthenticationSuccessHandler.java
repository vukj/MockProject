package com.fa.training.mockproject.socialsecurity.service;

import com.fa.training.mockproject.entities.UserAccountDetails;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import com.fa.training.mockproject.services.UserAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@Service
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserAccountsService userAccountsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Get information of user after login
        UserAccountDetails userAccountDetails = (UserAccountDetails) authentication.getPrincipal();
        // Get email
        String email = userAccountDetails.getEmail();
        // Get user
        UserAccountsLoginDTO user = userAccountsService.findByEmail(email);
        // Add user to session
        HttpSession session = request.getSession();
        //set last logged
        user.setLastLogged(new Date());
        userAccountsService.updateLastLogged(user);
        // Add user to session
        session.setAttribute("user", user);
        response.sendRedirect("/my-account");
    }

}