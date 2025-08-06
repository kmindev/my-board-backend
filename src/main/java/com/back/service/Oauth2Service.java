package com.back.service;

import com.back.exception.AuthorizationRequestRejectedException;
import com.back.exception.Oauth2ProviderNotProvideException;
import com.back.secuirty.oauth2.Oauth2Client;
import com.back.secuirty.oauth2.Oauth2ProviderType;
import com.back.secuirty.oauth2.Oauth2UserResponse;
import com.back.secuirty.oauth2.response.Oauth2TokenResponse;
import com.back.service.dto.UserAccountDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Oauth2Service {

    private final List<Oauth2Client> oauth2Clients;
    private final UserAccountService userAccountService;
    private final LoginService loginService;

    public ResponseEntity<Void> authorizeRequest(Oauth2ProviderType oauth2ProviderType) {
        Oauth2Client oauth2Client = findOauth2ClientByType(oauth2ProviderType);
        return oauth2Client.redirectToAuthorizationServer();
    }

    public void handleAuthorizationCallback(
            Oauth2ProviderType oauth2ProviderType,
            String code, String error, HttpServletRequest request
    ) {
        if (code == null) {
            throw new AuthorizationRequestRejectedException(error);
        }

        Oauth2Client oauth2Client = findOauth2ClientByType(oauth2ProviderType);

        // 1. 토큰 요청
        Oauth2TokenResponse tokenResponse = oauth2Client.requestToken(code);

        // 2. 회원 정보 요청
        Oauth2UserResponse userInfo = oauth2Client.requestUserInfo(tokenResponse.accessToken());

        // 3. 회원 정보 조회(없으면 강제로 회원가입)
        UserAccountDto user = userAccountService.findOrCreateForOauth2(userInfo);

        // 4. 로그인 처리(세션, Cookie 방식)
        loginService.login(user, request);
    }

    private Oauth2Client findOauth2ClientByType(Oauth2ProviderType type) {
        if (type == null) {
            throw new Oauth2ProviderNotProvideException();
        }

        return oauth2Clients.stream()
                .filter(client -> type.getOauthClient().isAssignableFrom(client.getClass()))
                .filter(Oauth2Client::supports)
                .findFirst()
                .orElseThrow(Oauth2ProviderNotProvideException::new);
    }

}
