package com.back.secuirty.coustomoauth;

import org.springframework.http.ResponseEntity;

public interface Oauth2Client {
    ResponseEntity<Void> redirectToAuthorizationServer();
    boolean supports();
}
