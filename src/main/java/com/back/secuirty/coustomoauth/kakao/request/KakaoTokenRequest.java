package com.back.secuirty.coustomoauth.kakao.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenRequest(
        @JsonProperty("grant_type") String grantType,
        @JsonProperty("client_id") String clientId,
        @JsonProperty("redirect_uri") String redirectUri,
        @JsonProperty("code") String code,
        @JsonProperty("client_secret") String clientSecret
) {

    public static KakaoTokenRequest of(String clientId, String redirectUri, String code, String clientSecret) {
        return new KakaoTokenRequest("authorization_code", clientId, redirectUri, code, clientSecret);
    }

}
