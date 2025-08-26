package com.back.auth.infrastructure.oauth2.naver;

import com.back.auth.infrastructure.oauth2.Oauth2Client;
import com.back.auth.infrastructure.oauth2.dto.Oauth2UserResponse;
import com.back.auth.infrastructure.oauth2.response.Oauth2TokenResponse;
import org.springframework.http.ResponseEntity;

public class NaverOauth2Client implements Oauth2Client {
    @Override
    public ResponseEntity<Void> redirectToAuthorizationServer() {
        return null;
    }

    @Override
    public Oauth2TokenResponse requestToken(String code) {
        return null;
    }

    @Override
    public boolean supports() {
        return false;
    }

    @Override
    public Oauth2UserResponse requestUserInfo(String accessToken) {
        return null;
    }

}
