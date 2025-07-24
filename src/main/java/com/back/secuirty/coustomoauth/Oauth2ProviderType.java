package com.back.secuirty.coustomoauth;

import com.back.secuirty.coustomoauth.google.GoogleOauth2Client;
import com.back.secuirty.coustomoauth.naver.NaverOauth2Client;
import com.back.secuirty.coustomoauth.kakao.KakaoOauth2Client;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Oauth2ProviderType {
    KAKAO("kakao", KakaoOauth2Client.class),
    NAVER("naver", NaverOauth2Client.class),
    GOOGLE("google", GoogleOauth2Client.class);

    private final String request;
    private final Class<? extends Oauth2Client> oauthClient;

    Oauth2ProviderType(String request, Class<? extends Oauth2Client> oauthClient) {
        this.request = request;
        this.oauthClient = oauthClient;
    }

    public static Oauth2ProviderType from(String request) {
        return Arrays.stream(Oauth2ProviderType.values())
                .filter(provider -> provider.request.equals(request))
                .findFirst()
                .orElse(null);
    }

}
