package com.back.service;

import com.back.exception.Oauth2ProviderNotProvideException;
import com.back.secuirty.coustomoauth.Oauth2Client;
import com.back.secuirty.coustomoauth.Oauth2ProviderType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Oauth2Service {

    private final List<Oauth2Client> oauth2Clients;

    public ResponseEntity<Void> authorizeRequest(Oauth2ProviderType oauth2ProviderType) {
        if (oauth2ProviderType == null) {
            throw new Oauth2ProviderNotProvideException();
        }
        Oauth2Client oauth2Client = findOauth2ClientByType(oauth2ProviderType);
        return oauth2Client.redirectToAuthorizationServer();
    }

    private Oauth2Client findOauth2ClientByType(Oauth2ProviderType type) {
        return oauth2Clients.stream()
                .filter(client -> type.getOauthClient().isAssignableFrom(client.getClass()))
                .filter(Oauth2Client::supports)
                .findFirst()
                .orElseThrow(Oauth2ProviderNotProvideException::new);
    }

}
