package com.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        // TODO: 인증 구현 후 세부 로직 작성
        return () -> Optional.of("kkm");
    }

}
