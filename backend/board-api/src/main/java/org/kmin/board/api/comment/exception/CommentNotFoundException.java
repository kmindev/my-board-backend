package org.kmin.board.api.comment.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class CommentNotFoundException extends ApplicationException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
