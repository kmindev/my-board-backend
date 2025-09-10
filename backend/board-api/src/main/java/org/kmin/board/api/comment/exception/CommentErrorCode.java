package org.kmin.board.api.comment.exception;

import lombok.Getter;
import org.kmin.global_utils.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentErrorCode implements ErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다."),
    USER_MISMATCH(HttpStatus.BAD_REQUEST, "작성자가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CommentErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
