package org.kmin.board.api.auth.infrastructure.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUrlManager {

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
