package com.back.secuirty.oauth2.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.back.config.JsonDataEncoder;
import com.back.config.TestRestClientConfig;
import com.back.exception.ApplicationException;
import com.back.exception.Oauth2TokenRequestException;
import com.back.exception.Oauth2UserRequestException;
import com.back.secuirty.oauth2.Oauth2ProviderType;
import com.back.secuirty.oauth2.Oauth2UserResponse;
import com.back.secuirty.oauth2.response.Oauth2TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@DisplayName("카카오 Oauth2 클라이언트")
class KakaoOauth2ClientTest {

    @Disabled("실제 API를 호출하므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealTest {

        @Autowired
        private RestClient restClient;

        @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
        private String kakaoAuthorizationUri;
        @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
        private String kakaoTokenUri;
        @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
        private String kakaoUserInfoUri;
        @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
        private String kakaoClientId;
        @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
        private String kakaoClientSecret;
        @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
        private String kakaoRedirectUri;

        @Value("${test.kakao.auth-code:}")
        private String authorizationCode;
        @Value("${test.kakao.access-token:}")
        private String accessToken;

        @DisplayName("인가 코드 요청: GET https://kauth.kakao.com/oauth/authorize")
        @Test
        void GET_OAUTH2_AUTHORIZATION_KAKAO() {
            // given
            String randomString = UUID.randomUUID().toString();
            String uri = UriComponentsBuilder
                    .fromUriString(kakaoAuthorizationUri)
                    .queryParam("client_id", kakaoClientId)
                    .queryParam("redirect_uri", kakaoRedirectUri)
                    .queryParam("response_type", "code")
                    .queryParam("state", randomString)
                    .build()
                    .toUriString();

            // when
            ResponseEntity<Void> responseEntity = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .toBodilessEntity();

            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
            assertThat(responseEntity.getHeaders().get("location")).contains("https://accounts.kakao.com/login");
        }

        @DisplayName("토큰 요청: POST https://kauth.kakao.com/oauth/token")
        @Test
        void GET_OAUTH2_TOKEN_KAKAO() {
            // given
            assumeTrue(!authorizationCode.isBlank(), "토큰 요청에 필요한 인가 코드가 없어 테스트를 건너뜁니다.");
            String uri = UriComponentsBuilder
                    .fromUriString(kakaoTokenUri)
                    .build()
                    .toUriString();
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "authorization_code");
            formData.add("client_id", kakaoClientId);
            formData.add("redirect_uri", kakaoRedirectUri);
            formData.add("code", authorizationCode); // 토큰 요청에 필요한 인가 코드
            formData.add("client_secret", kakaoClientSecret);

            // when
            ResponseEntity<Oauth2TokenResponse> responseEntity = restClient.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .toEntity(Oauth2TokenResponse.class);
            Oauth2TokenResponse responseBody = responseEntity.getBody();

            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.accessToken()).isNotNull();
            assertThat(responseBody.idToken()).isNull();
            assertThat(responseBody.expiresIn()).isInstanceOf(Integer.class);
            assertThat(responseBody.refreshToken()).isNotNull();
            assertThat(responseBody.refreshTokenExpiresIn()).isInstanceOf(Integer.class);
            assertThat(responseBody.scope()).isEqualTo("profile_nickname");
        }

        @DisplayName("사용자 정보 조회 요청: GET https://kapi.kakao.com/v2/user/me")
        @Test
        void GET_OAUTH2_USER_INFO_KAKAO() {
            // given
            assumeTrue(!accessToken.isBlank(), "사용자 정보 조회 요청에 필요한 ACCESS TOKEN이 없어 테스트를 건너뜁니다.");
            String uri = UriComponentsBuilder
                    .fromUriString(kakaoUserInfoUri)
                    .build()
                    .toUriString();

            // when
            ResponseEntity<KakaoOauth2UserResponse> responseEntity = restClient.get()
                    .uri(uri)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .toEntity(KakaoOauth2UserResponse.class);
            KakaoOauth2UserResponse responseBody = responseEntity.getBody();

            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.id()).isNotNull();
            assertThat(responseBody.connectedAt()).isNotNull();
            assertThat(responseBody.properties().nickname()).isNotNull();
            assertThat(responseBody.kakaoAccount().profile().nickname()).isNotNull();
            assertThat(responseBody.kakaoAccount().profile().isDefaultNickname()).isInstanceOf(Boolean.class);
            assertThat(responseBody.kakaoAccount().profileNicknameNeedsAgreement()).isInstanceOf(Boolean.class);
        }

    }

    @DisplayName("Mock 테스트")
    @Import({TestRestClientConfig.class, JsonDataEncoder.class})
    @Nested
    @RestClientTest(KakaoOauth2Client.class)
    class MockTest {

        @Autowired
        private KakaoOauth2Client sut;
        @Autowired
        private JsonDataEncoder jsonDataEncoder;
        @Autowired
        private MockRestServiceServer server;

        @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
        private String kakaoAuthorizationUri;
        @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
        private String kakaoTokenUri;
        @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
        private String kakaoUserInfoUri;
        @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
        private String kakaoClientId;
        @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
        private String kakaoClientSecret;
        @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
        private String kakaoRedirectUri;

        @DisplayName("redirectToAuthorizationServer() 메서드를 호출하면, 주어진 KakaoAuthorization 정보를 보고 RestClient 이용해 호출하고 응답을 그대로 반환한다.")
        @Test
        void givenKakaoAuthorizationInfo_whenRedirectToAuthorizationServer_thenReturnsKakaoResponse() {
            // Given
            String location = "https://accounts.kakao.com/login";
            String expectedUriPattern = kakaoAuthorizationUri +
                    "\\?client_id=" + Pattern.quote(kakaoClientId) +
                    "&redirect_uri=" + Pattern.quote(kakaoRedirectUri) +
                    "&response_type=" + Pattern.quote("code") +
                    "&state=.+";
            server.expect(requestTo(matchesPattern(expectedUriPattern)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.FOUND).location(URI.create(location)));

            // When
            ResponseEntity<Void> responseEntity = sut.redirectToAuthorizationServer();

            // Then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
            assertThat(responseEntity.getHeaders().getLocation()).hasToString(location);
        }

        @DisplayName("requestToken() 호출 시, Kakao 토큰 엔드포인트에 POST 요청을 보내고 응답을 반환한다.")
        @Test
        void givenAuthorizationCode_whenRequestToken_thenReturnsOauth2TokenResponse()
                throws JsonProcessingException {
            // Given
            String code = "토큰 요청 인가 코드";
            String requestUri = kakaoTokenUri;
            Oauth2TokenResponse expectedResponse = new Oauth2TokenResponse(
                    "bearer",
                    UUID.randomUUID().toString(),
                    null,
                    43199,
                    UUID.randomUUID().toString(),
                    5184000,
                    "account_email profile"
            );

            server.expect(requestTo(requestUri))
                    .andExpect(method(HttpMethod.POST))
                    .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andRespond(withSuccess(
                            jsonDataEncoder.encode(expectedResponse)
                            , MediaType.APPLICATION_JSON
                    ));

            // When
            Oauth2TokenResponse response = sut.requestToken(code);

            // Then
            assertThat(response).isEqualTo(expectedResponse);
        }

        @DisplayName("requestToken() 호출 시 응답이 null이면 예외가 발생한다.")
        @Test
        void givenNullTokenResponse_whenRequestToken_thenThrowsException() {
            // Given
            String code = "토큰 요청 인가 코드";
            String requestUri = kakaoTokenUri;
            server.expect(requestTo(requestUri))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withSuccess("", MediaType.APPLICATION_JSON)); // 빈 응답

            // When
            Oauth2TokenRequestException result = assertThrows(Oauth2TokenRequestException.class,
                    () -> sut.requestToken(code));

            // Then
            assertThat(result).isInstanceOf(ApplicationException.class);
        }

        @DisplayName("requestToken() 호출 시 accessToken이 null이면 예외가 발생한다.")
        @Test
        void givenResponseWithoutAccessToken_whenRequestToken_thenThrowsException() throws JsonProcessingException {
            // Given
            String code = "토큰 요청 인가 코드";
            String uri = kakaoTokenUri;

            Oauth2TokenResponse invalidResponse = new Oauth2TokenResponse(
                    "bearer", null, null, 3600, "refreshToken", 3600, "scope"
            );

            server.expect(requestTo(uri))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withSuccess(jsonDataEncoder.encode(invalidResponse), MediaType.APPLICATION_JSON));

            // When
            Oauth2TokenRequestException result = assertThrows(Oauth2TokenRequestException.class,
                    () -> sut.requestToken(code));

            // Then
            assertThat(result).isInstanceOf(ApplicationException.class);
        }

        @DisplayName("requestUserInfo() 호출 시, Kakao 사용자 정보 엔드포인트에 GET 요청을 보내고 응답을 반환한다.")
        @Test
        void givenAccessToken_whenRequestUserInfo_thenReturnsOauth2UserResponse()
                throws JsonProcessingException {
            // Given
            String accessToken = "access-token-value";
            String requestUri = kakaoUserInfoUri;
            KakaoOauth2UserResponse expectedResponse = new KakaoOauth2UserResponse(
                    123456789L,
                    LocalDateTime.now(),
                    new KakaoOauth2UserResponse.Properties("nickname-value"),
                    new KakaoOauth2UserResponse.KakaoAccount(
                            new KakaoOauth2UserResponse.KakaoAccount.Profile(
                                    "nickname-value",
                                    false
                            ),
                            false

                    )
            );
            server.expect(requestTo(requestUri))
                    .andExpect(method(HttpMethod.GET))
                    .andExpect(header("Authorization", "Bearer " + accessToken))
                    .andRespond(withSuccess(
                            jsonDataEncoder.encode(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));

            // When
            Oauth2UserResponse response = sut.requestUserInfo(accessToken);

            // Then
            assertThat(response.id()).isEqualTo(expectedResponse.id());
            assertThat(response.nickname()).isEqualTo(expectedResponse.nickname());
            assertThat(response.oauth2ProviderType()).isEqualTo(Oauth2ProviderType.KAKAO);
        }

        @DisplayName("requestUserInfo() 호출 시 응답이 null이면 예외가 발생한다.")
        @Test
        void givenNullUserInfo_whenRequestUserInfo_thenThrowsException() {
            // Given
            String accessToken = "access-token";
            String uri = kakaoUserInfoUri;

            server.expect(requestTo(uri))
                    .andExpect(method(HttpMethod.GET))
                    .andExpect(header("Authorization", "Bearer " + accessToken))
                    .andRespond(withSuccess("", MediaType.APPLICATION_JSON)); // 빈 응답

            // When
            Oauth2UserRequestException result = assertThrows(Oauth2UserRequestException.class,
                    () -> sut.requestUserInfo(accessToken));

            // Then
            assertThat(result).isInstanceOf(ApplicationException.class);
        }

        @DisplayName("requestUserInfo() 호출 시 사용자 id 또는 nickname이 null이면 예외가 발생한다.")
        @Test
        void givenUserInfoWithoutIdOrNickname_whenRequestUserInfo_thenThrowsException() throws JsonProcessingException {
            // Given
            String accessToken = "access-token";
            String uri = kakaoUserInfoUri;

            KakaoOauth2UserResponse invalidResponse = new KakaoOauth2UserResponse(
                    null, // id null
                    LocalDateTime.now(),
                    new KakaoOauth2UserResponse.Properties(null), // nickname null
                    new KakaoOauth2UserResponse.KakaoAccount(
                            new KakaoOauth2UserResponse.KakaoAccount.Profile(null, false),
                            false
                    )
            );

            server.expect(requestTo(uri))
                    .andExpect(method(HttpMethod.GET))
                    .andExpect(header("Authorization", "Bearer " + accessToken))
                    .andRespond(withSuccess(jsonDataEncoder.encode(invalidResponse), MediaType.APPLICATION_JSON));

            // When
            Oauth2UserRequestException result = assertThrows(Oauth2UserRequestException.class,
                    () -> sut.requestUserInfo(accessToken));

            // Then
            assertThat(result).isInstanceOf(ApplicationException.class);
        }
    }

}