package org.kmin.board.api.apps.user.application;

import static org.kmin.board.api.common.fixture.UserAccountFixture.createDBUserAccountFromUserId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.kmin.board.domain.user.UserAccount;
import org.kmin.board.domain.user.UserRoleType;
import org.kmin.board.api.apps.user.application.exception.UserNotFoundException;
import org.kmin.board.domain.user.repository.UserAccountRepository;
import org.kmin.board.api.apps.auth.oauth2.domain.Oauth2ProviderType;
import org.kmin.board.api.apps.auth.oauth2.infrastrcture.oauth2.dto.Oauth2UserResponse;
import org.kmin.board.api.apps.user.application.dto.UserAccountDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("비즈니스 로직 - 회원")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    private UserAccountService sut;

    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("존재하는 유저 id를 전달하면, UserAccount 엔티티를 반환한다.")
    @Test
    void givenExitingUserId_whenGetUserAccount_thenReturnsUserAccount() {
        // Given
        String userId = "user1";
        UserAccount userAccount = createDBUserAccountFromUserId(userId);
        given(userAccountRepository.findById(userId)).willReturn(Optional.of(userAccount));

        // When
        UserAccount result = sut.getUserAccount(userId);

        // Then
        assertThat(result.getUserId()).isEqualTo(userAccount.getUserId());
        then(userAccountRepository).should().findById(userId);
    }

    @DisplayName("존재하지 않는 유저 id를 전달하면, UserNotFoundException 예외를 던진다.")
    @Test
    void givenNonExitingUserId_whenGetUserAccount_thenThrowsUserNotFoundException() {
        // Given
        String nonExistentUserId = "user2";
        given(userAccountRepository.findById(nonExistentUserId)).willReturn(Optional.empty());

        // When
        UserNotFoundException result = assertThrows(UserNotFoundException.class,
                () -> sut.getUserAccount(nonExistentUserId)
        );

        // Then
        assertThat(result).isInstanceOf(UserNotFoundException.class);
        then(userAccountRepository).should().findById(nonExistentUserId);
    }

    @DisplayName("Oauth2 유저 정보로 이미 가입된 사용자가 존재하면, 해당 사용자 정보를 반환한다.")
    @Test
    void givenExistingUser_whenFindOrCreateForOauth2_thenReturnsExistingUser() {
        // Given
        Oauth2UserResponse response = new Oauth2UserResponse(112312L, "nickname1", Oauth2ProviderType.KAKAO);
        UserAccount existingUser = UserAccount.createOAuth2UserAccount(
                response.userId(),
                "pw",
                null,
                response.nickname(),
                null,
                response.registrationId(),
                response.providerId(),
                UserRoleType.USER
        );

        given(userAccountRepository.findById(response.userId())).willReturn(Optional.of(existingUser));

        // When
        UserAccountDto result = sut.findOrCreateForOauth2(response);

        // Then
        assertThat(result.userId()).isEqualTo(existingUser.getUserId());
        assertThat(result.nickname()).isEqualTo(existingUser.getNickname());
        then(userAccountRepository).should().findById(response.userId());
        then(userAccountRepository).shouldHaveNoMoreInteractions();
    }

    @DisplayName("Oauth2 유저 정보로 가입된 사용자가 없으면, 새로 생성 후 저장하여 반환한다.")
    @Test
    void givenNewUser_whenFindOrCreateForOauth2_thenCreatesAndReturnsUser() {
        // Given
        Oauth2UserResponse response = new Oauth2UserResponse(2L, "new-user", Oauth2ProviderType.KAKAO);
        String newUserId = response.userId();

        UserAccount newUser = response.toEntity();

        given(userAccountRepository.findById(newUserId)).willReturn(Optional.empty());
        given(userAccountRepository.save(any(UserAccount.class))).willReturn(newUser);

        // When
        UserAccountDto result = sut.findOrCreateForOauth2(response);

        // Then
        assertThat(result.userId()).isEqualTo(newUser.getUserId());
        assertThat(result.nickname()).isEqualTo(newUser.getNickname());
        then(userAccountRepository).should().findById(newUserId);
        then(userAccountRepository).should().save(any(UserAccount.class));
    }

}