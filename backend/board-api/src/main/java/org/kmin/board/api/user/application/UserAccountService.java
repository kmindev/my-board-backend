package org.kmin.board.api.user.application;

import org.kmin.board.domain.user.UserAccount;
import org.kmin.board.domain.user.UserRoleType;
import org.kmin.board.api.user.exception.UserNotFoundException;
import org.kmin.board.domain.user.repository.UserAccountRepository;
import org.kmin.board.api.auth.infrastructure.oauth2.dto.Oauth2UserResponse;
import org.kmin.board.api.user.application.dto.UserAccountDto;
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
