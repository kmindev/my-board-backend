package org.kmin.board.api.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import org.kmin.board.api.user.application.UserAccountService;
import org.kmin.board.domain.user.UserRoleType;
import org.kmin.global_utils.exception.ApplicationException;
import org.kmin.board.api.auth.exception.AuthorizationRequestRejectedException;
import org.kmin.board.api.auth.exception.Oauth2ProviderNotProvideException;
import org.kmin.board.api.auth.domain.Oauth2ProviderType;
import org.kmin.board.api.auth.infrastructure.oauth2.dto.Oauth2UserResponse;
import org.kmin.board.api.auth.infrastructure.oauth2.google.GoogleOauth2Client;
import org.kmin.board.api.auth.infrastructure.oauth2.kakao.KakaoOauth2Client;
import org.kmin.board.api.auth.infrastructure.oauth2.naver.NaverOauth2Client;
import org.kmin.board.api.auth.infrastructure.oauth2.response.Oauth2TokenResponse;
import org.kmin.board.api.user.application.dto.UserAccountDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("서비스 로직 - Oauth2")
@ExtendWith(MockitoExtension.class)
class Oauth2ServiceTest {

    private Oauth2Service sut;

    @Mock
    private KakaoOauth2Client kakaoClient;

    @Mock
    private NaverOauth2Client naverClient;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private LoginService loginService;

    @Mock
    private GoogleOauth2Client googleOauth2Client;

    @BeforeEach
    void setUp() {
        sut = new Oauth2Service(List.of(kakaoClient), userAccountService, loginService);
    }

    @DisplayName("Provider 타입에 해당되는 Oauth2 클라이언트를 찾고, 지원한다면 요청을 지시한다.")
    @Test
    void givenProviderType_whenAuthorizeRequest_thenReturnsResponseEntity() {
        // give
        Oauth2ProviderType providerType = Oauth2ProviderType.KAKAO;
        given(kakaoClient.supports()).willReturn(true);
        given(kakaoClient.redirectToAuthorizationServer()).willReturn(ResponseEntity.status(HttpStatus.FOUND).build());

        // when
        ResponseEntity<Void> responseEntity = sut.authorizeRequest(providerType);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        then(kakaoClient).should().supports();
        then(kakaoClient).should().redirectToAuthorizationServer();
    }


    @DisplayName("Provider 타입에 해당되는 Oauth2 클라이언트를 찾았는데 지원하지 않는다면 예외를 발생시킨다.")
    @Test
    void givenNotSupportedProvider_whenAuthorizeRequest_thenThrowsException() {
        // give
        Oauth2ProviderType providerType = Oauth2ProviderType.KAKAO;
        given(kakaoClient.supports()).willReturn(false);

        // when
        Oauth2ProviderNotProvideException exception = assertThrows(
                Oauth2ProviderNotProvideException.class, () -> sut.authorizeRequest(providerType));

        // then
        assertThat(exception).isInstanceOf(Oauth2ProviderNotProvideException.class);
        then(kakaoClient).should().supports();
        then(kakaoClient).shouldHaveNoMoreInteractions();
    }

    @DisplayName("인가 코드가 주어지면 - 사용자 정보 조회 후 로그인 처리를 수행한다.")
    @Test
    void givenValidAuthorizationCode_whenHandleAuthorizationCallback_thenLoginUser() {
        // given
        Oauth2ProviderType providerType = Oauth2ProviderType.KAKAO;
        String code = "auth-code";
        String error = null;
        String accessToken = "access-token";
        HttpServletRequest request = mock(HttpServletRequest.class);
        Oauth2TokenResponse tokenResponse = new Oauth2TokenResponse(
                "bearer", "access-token", null, 3600, "refresh-token", 86400, "profile_nickname"
        );
        Oauth2UserResponse userResponse = new Oauth2UserResponse(123456789L, "nickname", providerType);
        UserAccountDto userAccountDto = UserAccountDto.of(
                userResponse.userId(), "encoded-pw", null, userResponse.nickname(), null,
                userResponse.registrationId(), userResponse.providerId(), UserRoleType.USER
        );

        given(kakaoClient.supports()).willReturn(true);
        given(kakaoClient.requestToken(code)).willReturn(tokenResponse);
        given(kakaoClient.requestUserInfo(accessToken)).willReturn(userResponse);
        given(userAccountService.findOrCreateForOauth2(userResponse)).willReturn(userAccountDto);

        // when
        sut.handleAuthorizationCallback(providerType, code, error, request);

        // then
        then(kakaoClient).should().requestToken(code);
        then(kakaoClient).should().requestUserInfo(accessToken);
        then(userAccountService).should().findOrCreateForOauth2(userResponse);
        then(loginService).should().login(userAccountDto, request);
    }

    @DisplayName("인가 코드가 없으면 예외를 던진다.")
    @Test
    void givenNullAuthorizationCode_whenHandleAuthorizationCallback_thenThrowException() {
        // given
        Oauth2ProviderType providerType = Oauth2ProviderType.KAKAO;
        String code = null;
        String error = "access_denied";
        HttpServletRequest request = mock(HttpServletRequest.class);

        // when & then
        AuthorizationRequestRejectedException exception = assertThrows(
                AuthorizationRequestRejectedException.class,
                () -> sut.handleAuthorizationCallback(providerType, code, error, request));

        // then
        assertThat(exception).isInstanceOf(ApplicationException.class);
    }

}