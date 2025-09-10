package org.kmin.board.api.apps.auth.basic.application;

import org.kmin.board.api.apps.auth.basic.domain.BoardUserDetails;
import org.kmin.board.api.apps.user.application.dto.UserAccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public void login(UserAccountDto userAccountDto, HttpServletRequest request) {
        BoardUserDetails boardUserDetails = BoardUserDetails.from(userAccountDto);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                boardUserDetails, null, boardUserDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

}
