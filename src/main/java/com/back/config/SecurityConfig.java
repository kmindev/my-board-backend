package com.back.config;

import static com.back.secuirty.SecurityUrlManager.ADMIN_ONLY_URLS;
import static com.back.secuirty.SecurityUrlManager.H2_CONSOLE_URL;
import static com.back.secuirty.SecurityUrlManager.LOGIN_URL;
import static com.back.secuirty.SecurityUrlManager.LOGOUT_URL;
import static com.back.secuirty.SecurityUrlManager.SWAGGER_URLS;
import static org.springframework.http.HttpMethod.GET;

import com.back.domain.UserRoleType;
import com.back.secuirty.general.ApiAuthenticationFilter;
import com.back.secuirty.general.handler.ApiAccessDeniedHandler;
import com.back.secuirty.general.handler.ApiAuthenticationFailureHandler;
import com.back.secuirty.general.handler.ApiAuthenticationSuccessHandler;
import com.back.secuirty.general.handler.ApiLoginAuthenticationEntryPoint;
import com.back.secuirty.general.handler.ApiLogoutSuccessHandler;
import com.back.secuirty.oauth2.handler.OAuth2AuthFailureHandler;
import com.back.secuirty.oauth2.handler.Oauth2AuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

@Profile("!test")
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
