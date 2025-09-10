package org.kmin.board.api.auth.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.kmin.board.api.common.config.JsonDataEncoder;
import org.kmin.board.api.auth.exception.AuthorizationRequestRejectedException;
import org.kmin.board.api.auth.domain.Oauth2ProviderType;
import org.kmin.board.api.auth.application.Oauth2Service;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("컨트롤러 - OAuth2")
@Import({JsonDataEncoder.class})
@WebMvcTest(controllers = Oauth2Controller.class)
@AutoConfigureMockMvc(addFilters = false)
class Oauth2ControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonDataEncoder jsonDataEncoder;
    @MockitoBean
    private Oauth2Service oauth2Service;

    @DisplayName("OAuth2 인증 요청 시 해당 provider의 authorization uri로 요청한 후 응답(리다이렉트) 그대로 반환한다.")
    @Test
    void givenOauth2ProviderType_whenAuthorize_thenReturnsRedirectResponse() throws Exception {
        // Given
        Oauth2ProviderType oauth2ProviderType = Oauth2ProviderType.KAKAO;
        ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.FOUND).build();
        given(oauth2Service.authorizeRequest(oauth2ProviderType)).willReturn(responseEntity);

        // When & Then
        mvc.perform(get("/oauth2/authorization/{providerType}", oauth2ProviderType.getRequest()))
                .andExpect(status().isFound());
        then(oauth2Service).should().authorizeRequest(oauth2ProviderType);
    }

    @DisplayName("로그인 콜백 성공 처리")
    @Test
    void givenValidCode_whenAuthorizationCallback_thenReturns200() throws Exception {
        // Given
        Oauth2ProviderType oauth2ProviderType = Oauth2ProviderType.KAKAO;
        String code = "auth-code";
        willDoNothing().given(oauth2Service).handleAuthorizationCallback(
                eq(oauth2ProviderType), eq(code), isNull(), any(HttpServletRequest.class)
        );

        // When & Then
        mvc.perform(get("/login/oauth2/code/{providerType}", oauth2ProviderType.getRequest())
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("로그인 성공"));
        then(oauth2Service).should().handleAuthorizationCallback(
                eq(oauth2ProviderType), eq(code), isNull(), any(HttpServletRequest.class)
        );
    }

    @DisplayName("로그인 콜백 실패")
    @Test
    void givenErrorParam_whenAuthorizationCallback_thenReturns5xx() throws Exception {
        // Given
        Oauth2ProviderType oauth2ProviderType = Oauth2ProviderType.KAKAO;
        String error = "access_denied";
        willThrow(new AuthorizationRequestRejectedException(error)).given(oauth2Service).handleAuthorizationCallback(
                eq(oauth2ProviderType), isNull(), eq(error), any(HttpServletRequest.class)
        );

        // When & Then
        mvc.perform(get("/login/oauth2/code/{providerType}", oauth2ProviderType.getRequest())
                        .param("error", error))
                .andExpect(status().is5xxServerError());
        then(oauth2Service).should().handleAuthorizationCallback(
                eq(oauth2ProviderType), isNull(), eq(error), any(HttpServletRequest.class)
        );
    }

}