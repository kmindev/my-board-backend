package com.back.secuirty.coustomoauth.naver;

import com.back.secuirty.coustomoauth.Oauth2Client;
import org.springframework.http.ResponseEntity;

public class NaverOauth2Client implements Oauth2Client  {
    @Override
    public ResponseEntity<Void> redirectToAuthorizationServer() {
        return null;
    }

    @Override
    public boolean supports() {
        return false;
    }
}
