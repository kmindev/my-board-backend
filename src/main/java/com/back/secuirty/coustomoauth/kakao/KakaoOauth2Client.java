package com.back.secuirty.coustomoauth.kakao;

import com.back.secuirty.coustomoauth.Oauth2Client;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    public boolean supports() {
        return true;
    }

}
