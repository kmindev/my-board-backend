package org.kmin.board.api.common.config;

import static org.kmin.board.api.common.config.SecurityConfig.SecurityUrlManager.ADMIN_ONLY_URLS;
import static org.kmin.board.api.common.config.SecurityConfig.SecurityUrlManager.H2_CONSOLE_URL;
import static org.kmin.board.api.common.config.SecurityConfig.SecurityUrlManager.LOGIN_URL;
import static org.kmin.board.api.common.config.SecurityConfig.SecurityUrlManager.LOGOUT_URL;
import static org.kmin.board.api.common.config.SecurityConfig.SecurityUrlManager.SWAGGER_URLS;
import static org.springframework.http.HttpMethod.GET;

import lombok.RequiredArgsConstructor;
import org.kmin.board.api.apps.auth.common.ApiAccessDeniedHandler;
import org.kmin.board.api.apps.auth.common.ApiLoginAuthenticationEntryPoint;
import org.kmin.board.api.apps.auth.common.ApiLogoutSuccessHandler;
import org.kmin.board.api.apps.auth.basic.presentation.ApiAuthenticationFailureHandler;
import org.kmin.board.api.apps.auth.basic.presentation.ApiAuthenticationFilter;
import org.kmin.board.api.apps.auth.basic.presentation.ApiAuthenticationSuccessHandler;
import org.kmin.board.domain.user.UserRoleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final ApiAuthenticationSuccessHandler successHandler;
    private final ApiAuthenticationFailureHandler failureHandler;
    private final ApiAccessDeniedHandler deniedHandler;
    private final ApiLoginAuthenticationEntryPoint entryPoint;
    private final ApiLogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                .disable()) // H2 콘솔 표시를 위한 헤더 비활성화
            .authorizeHttpRequests(request -> request
                .requestMatchers(GET, "/v1/articles/**").permitAll()
                .requestMatchers(GET, "/v1/comments/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                .requestMatchers(SWAGGER_URLS).permitAll()
                .requestMatchers(H2_CONSOLE_URL).permitAll()
                .requestMatchers(LOGIN_URL).permitAll()
                .requestMatchers(ADMIN_ONLY_URLS).hasAuthority(UserRoleType.ADMIN.getName())
                .anyRequest().authenticated())
            .addFilterBefore(apiAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(entryPoint) // 인증되지 않은 요청 처리
                .accessDeniedHandler(deniedHandler) // 권한 부족 요청 처리
            )
            .logout(logout -> logout
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true) // 인증 정보 삭제
                .deleteCookies("JSESSIONID")
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

    public static class SecurityUrlManager {
        public static final String LOGIN_URL = "/v1/auth/login";
        public static final String LOGOUT_URL = "/v1/auth/logout";
        public static final String H2_CONSOLE_URL = "/h2-console/**";
        public static final String[] SWAGGER_URLS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
        };
        public static final String[] ADMIN_ONLY_URLS = {
            "/v1/auth/admin-test"
        };
    }

}
