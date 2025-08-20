package com.back.controler;

import static com.back.config.TestSecurityUtil.boardUserDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.back.config.JsonDataEncoder;
import com.back.config.SecurityConfig;
import com.back.controler.dto.BoardUserDetailsFixture;
import com.back.controler.dto.request.LoginRequest;
import com.back.domain.UserRoleType;
import com.back.secuirty.BoardUserDetails;
import com.back.secuirty.general.ApiAuthenticationProvider;
import com.back.secuirty.general.ApiAuthenticationToken;
import com.back.secuirty.general.handler.ApiAccessDeniedHandler;
import com.back.secuirty.general.handler.ApiAuthenticationFailureHandler;
import com.back.secuirty.general.handler.ApiAuthenticationSuccessHandler;
import com.back.secuirty.general.handler.ApiLoginAuthenticationEntryPoint;
import com.back.secuirty.general.handler.ApiLogoutSuccessHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("컨트롤러 - Auth")
@Import({
        JsonDataEncoder.class,
        SecurityConfig.class,
        AuthenticationConfiguration.class,
        ApiAuthenticationSuccessHandler.class,
        ApiAuthenticationFailureHandler.class,
        ApiAccessDeniedHandler.class,
        ApiLoginAuthenticationEntryPoint.class,
        ApiLogoutSuccessHandler.class
})
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonDataEncoder jsonDataEncoder;

    @MockitoBean
    private ApiAuthenticationProvider apiAuthenticationProvider;

    @DisplayName("로그인 요청 - 성공")
    @Test
    void givenNewArticleRequest_whenNewArticle_thenReturns200() throws Exception {
        // Given
        String userId = "user1";
        String password = "pass1";
        LoginRequest request = new LoginRequest(userId, password);
        BoardUserDetails boardUserDetails = BoardUserDetailsFixture.boardUserDetails(userId, UserRoleType.USER);
        ApiAuthenticationToken apiAuthenticationToken = new ApiAuthenticationToken(boardUserDetails, null,
                boardUserDetails.getAuthorities());
        given(apiAuthenticationProvider.supports(any())).willReturn(true);
        given(apiAuthenticationProvider.authenticate(any())).willReturn(apiAuthenticationToken);

        // When & Then
        mvc.perform(post("/v1/auth/login")
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("로그인 성공."));
        then(apiAuthenticationProvider).should().supports(any());
        then(apiAuthenticationProvider).should().authenticate(any());
    }

    @DisplayName("로그인 요청 - 실패(ID 가 잘못된 경우)")
    @Test
    void givenInvalidUserId_whenAuthenticate_thenReturns4xx() throws Exception {
        // Given
        String invalidUserId = "invalid-user1";
        String password = "pass1";
        LoginRequest request = new LoginRequest(invalidUserId, password);
        UsernameNotFoundException exception = new UsernameNotFoundException("ID 가 존재하지 않습니다.");
        given(apiAuthenticationProvider.supports(any())).willReturn(true);
        given(apiAuthenticationProvider.authenticate(any())).willThrow(exception);

        // When & Then
        mvc.perform(post("/v1/auth/login")
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(apiAuthenticationProvider).should().supports(any());
        then(apiAuthenticationProvider).should().authenticate(any());
    }

    @DisplayName("로그인 요청 - 실패(패스워드가 잘못된 경우)")
    @Test
    void givenInvalidPassword_whenAuthenticate_thenReturns4xx() throws Exception {
        // Given
        String userId = "user1";
        String invalidPassword = "invalid-pass1";
        LoginRequest request = new LoginRequest(userId, invalidPassword);
        BadCredentialsException exception = new BadCredentialsException("패스워드가 일치하지 않습니다.");
        given(apiAuthenticationProvider.supports(any())).willReturn(true);
        given(apiAuthenticationProvider.authenticate(any())).willThrow(exception);

        // When & Then
        mvc.perform(post("/v1/auth/login")
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(apiAuthenticationProvider).should().supports(any());
        then(apiAuthenticationProvider).should().authenticate(any());
    }

    @DisplayName("로그아웃 요청 - 성공")
    @Test
    void givenAuthenticatedUser_whenLogout_thenReturns200AndClearsSession() throws Exception {
        mvc.perform(post("/v1/auth/logout")
                        .with(boardUserDetails("user1", UserRoleType.USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("로그아웃 성공"))
                .andExpect(cookie().maxAge("JSESSIONID", 0));
    }

}