package org.kmin.board.api.auth.infrastructure.security;

import static org.kmin.board.api.common.util.ResponseUtils.sendResponseWithBody;

import org.kmin.board.api.common.presentation.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 로그인 하지 않은 사용자가 접근했을 때 호출되는 핸들러
 */
@Slf4j
@Component
public class ApiLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.debug("[Authentication Required] url: {}, username: {}, msg: {}", request.getRequestURL(),
                request.getAttribute("username"), authException.getMessage());
        ApiResponse<Void> apiResponse = ApiResponse.errorWithMessage(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        sendResponseWithBody(response, apiResponse);
    }

}
