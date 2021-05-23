package com.fa.training.mockproject.configurations;

import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Autowired
    private HttpSession httpSession;

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserAccountsLoginDTO user = (UserAccountsLoginDTO) httpSession.getAttribute("user");
                    Optional<String> ex = Optional.ofNullable(user.getEmail());
                    return ex;
                }
                return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
            }
        };
/*
          if you are using spring security, you can get the currently logged username with following code segment.
          SecurityContextHolder.getContext().getAuthentication().getName()
         */
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}

