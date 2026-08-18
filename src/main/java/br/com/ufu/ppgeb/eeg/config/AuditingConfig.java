package br.com.ufu.ppgeb.eeg.config;


import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Configuration
@EnableJpaAuditing( auditorAwareRef = "auditorProvider" )
public class AuditingConfig {

    @Bean
    public AuditorAware< String > auditorProvider() {

        return () -> Optional.ofNullable( SecurityContextHolder.getContext() )
            .map( context -> context.getAuthentication() )
            .filter( Authentication::isAuthenticated )
            .map( Authentication::getName );
    }
}
