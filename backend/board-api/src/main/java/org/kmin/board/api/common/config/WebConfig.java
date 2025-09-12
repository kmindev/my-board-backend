package org.kmin.board.api.common.config;

import org.kmin.board.api.apps.auth.oauth2.domain.Oauth2ProviderTypeConverter;
import org.kmin.board.api.common.config.filter.ExecutionTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Oauth2ProviderTypeConverter());
    }

    @Bean
    public FilterRegistrationBean<ExecutionTimeFilter> requestTimeFilter() {
        FilterRegistrationBean<ExecutionTimeFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ExecutionTimeFilter());
        registration.addUrlPatterns("/*"); // 모든 요청
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE); // 필터 순서
        return registration;
    }

}
