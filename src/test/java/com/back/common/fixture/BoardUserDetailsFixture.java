package com.back.common.fixture;

import com.back.auth.domain.BoardUserDetails;
import com.back.user.domain.UserRoleType;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class BoardUserDetailsFixture {

    public static BoardUserDetails boardUserDetails(String username, UserRoleType roleType) {
        return new BoardUserDetails(
                username, "1234", null, null, null,
                null, null, roleType, List.of(new SimpleGrantedAuthority(roleType.getName())), Map.of()
        );
    }

}
