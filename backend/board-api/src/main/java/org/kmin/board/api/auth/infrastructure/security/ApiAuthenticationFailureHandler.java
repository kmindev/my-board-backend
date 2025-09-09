package org.kmin.board.api.auth.infrastructure.security;

import static org.kmin.board.api.common.util.ResponseUtils.sendResponseWithBody;

import org.kmin.board.api.common.presentation.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 인증 실패 시 호출되는 핸들러
 */
@Slf4j
@Component
public class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        String failureMessage = exception.getMessage();
        log.debug("[Authentication Failed] url: {}, username: {}, msg: {}", request.getRequestURL(),
                request.getAttribute("username"), failureMessage);
        ApiResponse<Void> apiResponse = ApiResponse.errorWithMessage(HttpStatus.BAD_REQUEST, failureMessage);
        sendResponseWithBody(response, apiResponse);
    }

}
