package com.back.auth.infrastructure.oauth2.kakao;

import com.back.auth.domain.exception.Oauth2TokenRequestException;
import com.back.auth.domain.exception.Oauth2UserRequestException;
import com.back.auth.infrastructure.oauth2.Oauth2Client;
import com.back.auth.infrastructure.oauth2.dto.Oauth2UserResponse;
import com.back.auth.infrastructure.oauth2.response.Oauth2TokenResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class KakaoOauth2Client implements Oauth2Client {

    private final RestClient restClient;

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

    @Override
    public ResponseEntity<Void> redirectToAuthorizationServer() {
        String uri = UriComponentsBuilder
                .fromUriString(kakaoAuthorizationUri)
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", kakaoRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("state", UUID.randomUUID().toString())
                .build()
                .toUriString();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Oauth2TokenResponse requestToken(String code) {
        String uri = UriComponentsBuilder
                .fromUriString(kakaoTokenUri)
                .build()
                .toUriString();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", kakaoClientId);
        formData.add("redirect_uri", kakaoRedirectUri);
        formData.add("code", code);
        formData.add("client_secret", kakaoClientSecret);

        ResponseEntity<Oauth2TokenResponse> responseEntity = restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .toEntity(Oauth2TokenResponse.class);
        Oauth2TokenResponse responseBody = responseEntity.getBody();
        validateTokenResponse(responseBody);
        return responseBody;
    }

    @Override
    public Oauth2UserResponse requestUserInfo(String accessToken) {
        String uri = UriComponentsBuilder
                .fromUriString(kakaoUserInfoUri)
                .build()
                .toUriString();
        ResponseEntity<KakaoOauth2UserResponse> responseEntity = restClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(KakaoOauth2UserResponse.class);
        KakaoOauth2UserResponse response = responseEntity.getBody();
        validateOauth2UserResponse(response);
        return response.toOauth2Response();
    }

    private void validateOauth2UserResponse(KakaoOauth2UserResponse response) {
        if (response == null || response.id() == null || response.nickname() == null) {
            throw new Oauth2UserRequestException();
        }
    }

    private void validateTokenResponse(Oauth2TokenResponse responseBody) {
        if (responseBody == null || responseBody.accessToken() == null) {
            throw new Oauth2TokenRequestException();
        }
    }

    @Override
    public boolean supports() {
        return true;
    }

}
