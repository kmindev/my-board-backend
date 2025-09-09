package org.kmin.board.api.auth.infrastructure.security;

import static org.kmin.board.api.common.util.ResponseUtils.sendResponseWithBody;

import org.kmin.board.api.common.presentation.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if (authentication == null) {
            log.debug("[Logout Succeed] 이미 로그아웃 된 유저입니다.");
        } else {
            log.debug("[Logout Succeed] username: {}", authentication.getName());
        }
        ApiResponse<Void> apiResponse = ApiResponse.okWithMessage("로그아웃 성공");
        sendResponseWithBody(response, apiResponse);
    }

}
