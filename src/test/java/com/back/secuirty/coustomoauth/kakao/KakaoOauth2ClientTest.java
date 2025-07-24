package com.back.secuirty.coustomoauth.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.back.config.TestRestClientConfig;
import java.net.URI;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@DisplayName("카카오 Oauth 클라이언트")
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
        @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
        private String kakaoClientId;
        @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
        private String kakaoClientSecret;
        @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
        private String kakaoRedirectUri;

        @DisplayName("GET /oauth2/authorization/kakao")
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
    }

    @DisplayName("Mock 테스트")
    @Import({TestRestClientConfig.class})
    @Nested
    @RestClientTest(KakaoOauth2Client.class)
    class MockTest {

        @Autowired
        private KakaoOauth2Client sut;
        @Autowired
        private MockRestServiceServer server;

        @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
        private String kakaoAuthorizationUri;
        @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
        private String kakaoTokenUri;
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
    }

}