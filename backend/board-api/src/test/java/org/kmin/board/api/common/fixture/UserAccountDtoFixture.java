package org.kmin.board.api.common.fixture;

import org.kmin.board.api.user.application.dto.UserAccountDto;
import org.kmin.board.domain.user.UserRoleType;

public class UserAccountDtoFixture {
    private static final String DEFAULT_USER_ID = "user1";
    private static final String DEFAULT_PASSWORD = "password1";
    private static final String DEFAULT_EMAIL = "user1@gmail.com";
    private static final String DEFAULT_NICKNAME = "닉네임1";

    /**
     * <p>
     * 기본값으로 구성된 {@link UserAccountDto} 객체를 생성합니다.
     * <ul>
     *   <li>userId: {@link UserAccountDtoFixture#DEFAULT_USER_ID}</li>
     *   <li>userPassword: {@link UserAccountDtoFixture#DEFAULT_PASSWORD}</li>
     *   <li>email: {@link UserAccountDtoFixture#DEFAULT_EMAIL}</li>
     *   <li>nickname: {@link UserAccountDtoFixture#DEFAULT_NICKNAME}</li>
     *   <li>role: {@link UserRoleType#USER}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link UserAccountDto} 객체
     */
    public static UserAccountDto createUserAccountDto() {
        return new UserAccountDto(
                DEFAULT_USER_ID, DEFAULT_PASSWORD, DEFAULT_EMAIL, DEFAULT_NICKNAME,
                null, null, null, UserRoleType.USER,
                null, null, null, null
        );
    }

}
