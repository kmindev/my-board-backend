package com.back.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.back.exception.Oauth2ProviderNotProvideException;
import com.back.secuirty.oauth2.Oauth2ProviderType;
import com.back.secuirty.oauth2.google.GoogleOauth2Client;
import com.back.secuirty.oauth2.kakao.KakaoOauth2Client;
import com.back.secuirty.oauth2.naver.NaverOauth2Client;
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

}