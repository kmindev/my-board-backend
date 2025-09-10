package org.kmin.board.api.common.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import org.kmin.board.domain.user.UserRoleType;
import org.kmin.board.api.apps.auth.basic.domain.BoardUserDetails;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class TestSecurityUtil {

    public static RequestPostProcessor boardUserDetails(String username, UserRoleType roleType) {
        BoardUserDetails boardUserDetails = new BoardUserDetails(
                username, "1234", null, null, null,
                null, null, roleType, List.of(new SimpleGrantedAuthority(roleType.getName())), Map.of()
        );
        return authentication(new UsernamePasswordAuthenticationToken(boardUserDetails, boardUserDetails.getPassword(),
                boardUserDetails.getAuthorities()));
    }

}
