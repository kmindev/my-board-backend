package org.kmin.board.api.auth.domain;

import org.kmin.board.api.user.domain.UserAccount;
import org.kmin.board.api.user.domain.UserRoleType;
import org.kmin.board.api.user.application.dto.UserAccountDto;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record BoardUserDetails(
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        String socialProvider,
        String socialId,
        UserRoleType role,
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> oAuth2Attributes
) implements UserDetails {

    public UserAccountDto toDto() {
        return UserAccountDto.of(userId, userPassword, email, nickname, memo, socialProvider, socialId, role);
    }

    public static BoardUserDetails from(UserAccount userAccount) {
        return new BoardUserDetails(
                userAccount.getUserId(),
                userAccount.getUserPassword(),
                userAccount.getEmail(),
                userAccount.getNickname(),
                userAccount.getMemo(),
                userAccount.getSocialProvider(),
                userAccount.getSocialId(),
                userAccount.getRole(),
                List.of(new SimpleGrantedAuthority(userAccount.getRole().getName())),
                Map.of()
        );
    }

    public static BoardUserDetails from(UserAccountDto userAccountDto) {
        return new BoardUserDetails(
                userAccountDto.userId(),
                userAccountDto.userPassword(),
                userAccountDto.email(),
                userAccountDto.nickname(),
                userAccountDto.memo(),
                userAccountDto.socialProvider(),
                userAccountDto.socialId(),
                userAccountDto.role(),
                List.of(new SimpleGrantedAuthority(userAccountDto.role().getName())),
                Map.of()
        );
    }

    public static BoardUserDetails of(UserAccountDto userAccountDto, Map<String, Object> oAuth2Attributes) {
        return new BoardUserDetails(
                userAccountDto.userId(),
                userAccountDto.userPassword(),
                userAccountDto.email(),
                userAccountDto.nickname(),
                userAccountDto.memo(),
                userAccountDto.socialProvider(),
                userAccountDto.socialId(),
                userAccountDto.role(),
                List.of(new SimpleGrantedAuthority(userAccountDto.role().getName())),
                oAuth2Attributes
        );
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
