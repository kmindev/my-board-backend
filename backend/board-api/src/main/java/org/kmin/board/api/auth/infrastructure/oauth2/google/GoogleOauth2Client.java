package org.kmin.board.api.auth.infrastructure.oauth2.google;

import org.kmin.board.api.auth.infrastructure.oauth2.Oauth2Client;
import org.kmin.board.api.auth.infrastructure.oauth2.dto.Oauth2UserResponse;
import org.kmin.board.api.auth.infrastructure.oauth2.response.Oauth2TokenResponse;
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
