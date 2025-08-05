package com.back.secuirty.coustomoauth;

import com.back.secuirty.coustomoauth.response.Oauth2TokenResponse;
import org.springframework.http.ResponseEntity;

public interface Oauth2Client {
    ResponseEntity<Void> redirectToAuthorizationServer();
    Oauth2TokenResponse requestToken(String code);
    boolean supports();
    Oauth2UserResponse requestUserInfo(String accessToken);
}
