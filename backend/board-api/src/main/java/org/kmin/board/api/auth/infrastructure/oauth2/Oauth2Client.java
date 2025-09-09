package org.kmin.board.api.auth.infrastructure.oauth2;

import org.kmin.board.api.auth.infrastructure.oauth2.dto.Oauth2UserResponse;
import org.kmin.board.api.auth.infrastructure.oauth2.response.Oauth2TokenResponse;
import org.springframework.http.ResponseEntity;

public interface Oauth2Client {
    ResponseEntity<Void> redirectToAuthorizationServer();
    Oauth2TokenResponse requestToken(String code);
    boolean supports();
    Oauth2UserResponse requestUserInfo(String accessToken);
}
