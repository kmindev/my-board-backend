package com.back.secuirty.coustomoauth.google;

import com.back.secuirty.coustomoauth.Oauth2Client;
import com.back.secuirty.coustomoauth.Oauth2UserResponse;
import com.back.secuirty.coustomoauth.response.Oauth2TokenResponse;
import org.springframework.http.ResponseEntity;

public class GoogleOauth2Client implements Oauth2Client {

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
