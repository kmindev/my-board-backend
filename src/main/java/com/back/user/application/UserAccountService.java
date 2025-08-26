package com.back.user.application;

import com.back.user.domain.UserAccount;
import com.back.user.domain.UserRoleType;
import com.back.user.domain.exception.UserNotFoundException;
import com.back.user.infrastructure.repository.UserAccountRepository;
import com.back.auth.infrastructure.oauth2.dto.Oauth2UserResponse;
import com.back.user.application.dto.UserAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public UserAccount getUserAccount(String userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserAccountDto saveUser(
            String userId, String password, String email, String nickname,
            String memo, String socialProvider, String socialId, UserRoleType role
    ) {
        UserAccount userAccount = UserAccount.createOAuth2UserAccount(userId, password, email, nickname, memo,
                socialProvider, socialId, role);
        return UserAccountDto.from(userAccountRepository.save(userAccount));
    }

    public UserAccountDto findOrCreateForOauth2(Oauth2UserResponse userInfo) {
        UserAccount user = userAccountRepository.findById(userInfo.userId())
                .orElseGet(() -> {
                    UserAccount userAccount = userInfo.toEntity();
                    return userAccountRepository.save(userAccount);
                });
        return UserAccountDto.from(user);
    }

}
