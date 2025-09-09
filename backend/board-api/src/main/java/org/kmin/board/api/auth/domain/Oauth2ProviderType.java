package org.kmin.board.api.auth.domain;

import org.kmin.board.api.auth.infrastructure.oauth2.Oauth2Client;
import org.kmin.board.api.auth.infrastructure.oauth2.google.GoogleOauth2Client;
import org.kmin.board.api.auth.infrastructure.oauth2.naver.NaverOauth2Client;
import org.kmin.board.api.auth.infrastructure.oauth2.kakao.KakaoOauth2Client;
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
