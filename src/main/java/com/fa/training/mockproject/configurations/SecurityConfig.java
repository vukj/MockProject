package com.fa.training.mockproject.configurations;

import com.fa.training.mockproject.services.UserAccountsService;
import com.fa.training.mockproject.socialsecurity.service.AuthenticationFailureHandler;
import com.fa.training.mockproject.socialsecurity.service.AuthenticationSuccessHandler;
import com.fa.training.mockproject.socialsecurity.service.CustomOAuth2UserService;
import com.fa.training.mockproject.socialsecurity.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAccountsService userAccountsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private CustomOidcUserService customOidcUserService;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userAccountsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /**
         * Access Page
         */
        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests().antMatchers("/", "/login", "/logout", "/oauth2").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/my-account").access("hasAnyRole('ROLE_STAFF','ROLE_ADMIN','ROLE_MANAGER','ROLE_LEADER')");
        httpSecurity.authorizeRequests().antMatchers("/my-profiles").access("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')");
        httpSecurity.authorizeRequests().antMatchers("/profile-pattern").access("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')");
        httpSecurity.authorizeRequests().antMatchers("/member-profiles").access("hasAnyRole('ROLE_LEADER','ROLE_MANAGER')");
//        httpSecurity.authorizeRequests().antMatchers("/member-profiles/printProfileAjax").access("hasAnyRole('ROLE_LEADER','ROLE_MANAGER')");

        /**
         * Config Login Form
         */
        httpSecurity.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/my-account")
                .loginPage("/signin")
                .defaultSuccessUrl("/my-account")
                .failureUrl("/login-error")
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutUrl("/signout")
                .logoutSuccessUrl("/")
                .and().exceptionHandling()
                .accessDeniedPage("/403")
                .and()
                // Config for Login Google
                .oauth2Login()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .oidcUserService(customOidcUserService);
        /**
         * Config Remember me
         */
        httpSecurity.rememberMe().key("uniqueAndSecret")
                .tokenValiditySeconds(1 * 24 * 60 * 60);


    }
}