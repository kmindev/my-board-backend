package org.kmin.board.api.auth.exception;

import lombok.Getter;
import org.kmin.global_utils.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements ErrorCode {

    // OAUTH2
    OAUTH2_PROVIDER_NOT_PROVIDE(HttpStatus.BAD_REQUEST, "해당 로그인은 지원하지 않습니다."),
    AUTHORIZATION_REQUEST_REJECTED(HttpStatus.BAD_GATEWAY, "OAUTH2 인가 요청이 거부되었습니다."),
    OAUTH2_TOKEN_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH2 액세스 토큰을 요청에 실패했습니다."),
    OAUTH2_USER_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH2 사용자 정보 요청에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
