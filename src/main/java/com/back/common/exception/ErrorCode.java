package com.back.common.exception;

import com.back.article.domain.SearchType;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Article - Comment
    UNEXPECTED_SEARCH_TYPE(HttpStatus.BAD_REQUEST, "잘못된 검색 타입입니다. 가능한 검색 타입: " +
            Arrays.stream(SearchType.values()).map(SearchType::getTypeName).toList()
    ),
    ARTICLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    USER_MISMATCH(HttpStatus.BAD_REQUEST, "작성자가 아닙니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다."),

    // UserAccount
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),

    // OAUTH2
    OAUTH2_PROVIDER_NOT_PROVIDE(HttpStatus.BAD_REQUEST, "해당 로그인은 지원하지 않습니다."),
    AUTHORIZATION_REQUEST_REJECTED(HttpStatus.BAD_GATEWAY, "OAUTH2 인가 요청이 거부되었습니다."),
    OAUTH2_TOKEN_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH2 액세스 토큰을 요청에 실패했습니다."),
    OAUTH2_USER_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH2 사용자 정보 요청에 실패했습니다."),

    //500 error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
