package com.back.secuirty.coustomoauth;

import com.back.domain.UserAccount;
import com.back.domain.UserRoleType;
import java.util.UUID;

public record Oauth2UserResponse(
        Long id,
        String nickname,
        Oauth2ProviderType oauth2ProviderType
) {

    public UserAccount toEntity() {
        return UserAccount.createOAuth2UserAccount(
                this.userId(),
                UUID.randomUUID().toString(),
                null,
                this.nickname,
                null,
                this.registrationId(),
                this.providerId(),
                UserRoleType.USER
        );
    }

    public String userId() {
        return this.oauth2ProviderType().getRequest().toLowerCase() + "_" + this.id();
    }

    public String providerId() {
        return String.valueOf(this.id);
    }

    public String registrationId() {
        return this.oauth2ProviderType().getRequest().toLowerCase();
    }

}
