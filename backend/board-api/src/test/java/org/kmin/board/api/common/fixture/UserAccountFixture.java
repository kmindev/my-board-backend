package org.kmin.board.api.common.fixture;

import org.kmin.board.domain.user.UserAccount;
import org.kmin.board.domain.user.UserRoleType;

public class UserAccountFixture {

    private static final String DEFAULT_USER_ID = "user1";
    private static final String DEFAULT_PASSWORD = "password1";
    private static final String DEFAULT_EMAIL = "user1@gmail.com";
    private static final String DEFAULT_NICKNAME = "닉네임1";

    /**
     * <p>
     * 기본값으로 구성된 {@link UserAccount} 객체를 생성합니다.
     * <ul>
     *   <li>userId: {@link UserAccountFixture#DEFAULT_USER_ID}</li>
     *   <li>password: {@link UserAccountFixture#DEFAULT_PASSWORD}</li>
     *   <li>email: {@link UserAccountFixture#DEFAULT_EMAIL}</li>
     *   <li>nickname: {@link UserAccountFixture#DEFAULT_NICKNAME}</li>
     *   <li>role: {@link UserRoleType#USER}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link UserAccount} 객체
     */
    public static UserAccount createDBUserAccount() {
        return UserAccount.of(
                DEFAULT_USER_ID,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL,
                DEFAULT_NICKNAME,
                null,
                UserRoleType.USER
        );
    }

    /**
     * <p>
     * 기본값으로 구성된 {@link UserAccount} 객체를 생성합니다.
     * <ul>
     *   <li>userId: {@param userId}</li>
     *   <li>password: {@link UserAccountFixture#DEFAULT_PASSWORD}</li>
     *   <li>email: {@link UserAccountFixture#DEFAULT_EMAIL}</li>
     *   <li>nickname: {@link UserAccountFixture#DEFAULT_NICKNAME}</li>
     *   <li>role: {@link UserRoleType#USER}</li>
     * </ul>
     * </p>
     *
     * @param userId 유저 id
     * @return 기본값으로 구성된 {@link UserAccount} 객체
     */
    public static UserAccount createDBUserAccountFromUserId(String userId) {
        return UserAccount.of(
                userId,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL,
                DEFAULT_NICKNAME,
                null,
                UserRoleType.USER
        );
    }

}
