package com.back.controler;

import com.back.secuirty.coustomoauth.Oauth2ProviderType;
import com.back.service.Oauth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class Oauth2Controller {

    private final Oauth2Service oauth2Service;

    @GetMapping("/oauth2/authorization/{oauth2ProviderType}")
    public ResponseEntity<Void> authorize(@PathVariable("oauth2ProviderType") Oauth2ProviderType oauth2ProviderType) {
        return oauth2Service.authorizeRequest(oauth2ProviderType);
    }

}
