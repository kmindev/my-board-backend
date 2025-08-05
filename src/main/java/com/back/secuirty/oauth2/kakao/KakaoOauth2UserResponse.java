package com.back.secuirty.oauth2.kakao;

import com.back.secuirty.oauth2.Oauth2ProviderType;
import com.back.secuirty.oauth2.Oauth2UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record KakaoOauth2UserResponse(
        Long id,
        @JsonProperty("connected_at") LocalDateTime connectedAt,
        Properties properties,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {

    public record KakaoAccount(
            Profile profile,
            @JsonProperty("profile_nickname_needs_agreement") Boolean profileNicknameNeedsAgreement

    ) {
        public record Profile(
                String nickname,
                @JsonProperty("is_default_nickname") Boolean isDefaultNickname
        ) {
        }
    }

    public record Properties(
            String nickname
    ) {
    }

    public String nickname() {
        return this.kakaoAccount.profile.nickname;
    }

    public Oauth2UserResponse toOauth2Response() {
        return new Oauth2UserResponse(this.id, this.nickname(), Oauth2ProviderType.KAKAO);
    }

}
