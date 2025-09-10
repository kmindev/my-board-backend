package org.kmin.board.api.apps.article.application.exception;

import java.util.Arrays;
import lombok.Getter;
import org.kmin.board.api.apps.article.presentation.dto.request.SearchType;
import org.kmin.board.api.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum ArticleErrorCode implements ErrorCode {

    UNEXPECTED_SEARCH_TYPE(HttpStatus.BAD_REQUEST, "잘못된 검색 타입입니다. 가능한 검색 타입: " +
        Arrays.stream(SearchType.values()).map(SearchType::getTypeName).toList()
    ),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    USER_MISMATCH(HttpStatus.BAD_REQUEST, "작성자가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ArticleErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
