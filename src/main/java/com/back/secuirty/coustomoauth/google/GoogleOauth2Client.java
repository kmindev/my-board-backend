package com.back.secuirty.coustomoauth.google;

import com.back.secuirty.coustomoauth.Oauth2Client;
import org.springframework.http.ResponseEntity;

public class GoogleOauth2Client implements Oauth2Client {

    @Override
    public ResponseEntity<Void> redirectToAuthorizationServer() {
        return null;
    }

    @Override
    public boolean supports() {
        return false;
    }

}
