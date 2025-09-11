package org.kmin.board.api.common.config;

import org.kmin.board.api.apps.auth.basic.domain.BoardUserDetails;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .filter(authentication -> authentication.getPrincipal() instanceof BoardUserDetails)
                .map(Authentication::getPrincipal)
                .map(BoardUserDetails.class::cast)
                .map(BoardUserDetails::getUsername);
    }

}
