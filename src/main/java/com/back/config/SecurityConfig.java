package com.back.config;

import com.back.domain.UserRoleType;
import com.back.secuirty.ApiAuthenticationFilter;
import com.back.secuirty.handler.ApiAccessDeniedHandler;
import com.back.secuirty.handler.ApiAuthenticationFailureHandler;
import com.back.secuirty.handler.ApiAuthenticationSuccessHandler;
import com.back.secuirty.handler.ApiLoginAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private static final String LOGIN_URL = "/v1/auth/login";
    private static final String[] ALL_PERMITTED_URLS = {LOGIN_URL, "/h2-console/**"};
    private static final String[] ADMIN_PERMITTED_URLS = {"/v1/auth/admin-test"};

    private final AuthenticationConfiguration authenticationConfiguration;
    private final ApiAuthenticationSuccessHandler successHandler;
    private final ApiAuthenticationFailureHandler failureHandler;
    private final ApiAccessDeniedHandler deniedHandler;
    private final ApiLoginAuthenticationEntryPoint entryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable()) // H2 콘솔 표시를 위한 헤더 비활성화
                .authorizeHttpRequests(request -> request
                        .requestMatchers(ALL_PERMITTED_URLS).permitAll()
                        .requestMatchers(ADMIN_PERMITTED_URLS).hasAuthority(UserRoleType.ADMIN.getName())
                        .anyRequest().authenticated())
                .addFilterBefore(apiAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(entryPoint) // 인증되지 않은 요청 처리
                        .accessDeniedHandler(deniedHandler) // 권한 부족 요청 처리
                );

        return http.build();
    }

    @Bean
    public ApiAuthenticationFilter apiAuthenticationFilter() throws Exception {
        ApiAuthenticationFilter filter = new ApiAuthenticationFilter(
                new AntPathRequestMatcher(LOGIN_URL, HttpMethod.POST.name()),
                authenticationConfiguration.getAuthenticationManager()
        );
        filter.setAuthenticationSuccessHandler(successHandler); // 인증 성공 핸들러
        filter.setAuthenticationFailureHandler(failureHandler); // 인증 실패 핸드러
        filter.setSecurityContextRepository(  // SecurityContext 저장소 설정
                new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(), // 요청 속성에 SecurityContext를 저장
                        new HttpSessionSecurityContextRepository() // 세션에 SecurityContext를 저장
                ));

        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
