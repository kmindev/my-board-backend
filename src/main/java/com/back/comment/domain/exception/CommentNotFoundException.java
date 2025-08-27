package com.back.comment.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

public class CommentNotFoundException extends ApplicationException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
