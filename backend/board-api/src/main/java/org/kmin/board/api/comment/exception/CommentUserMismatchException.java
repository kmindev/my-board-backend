package org.kmin.board.api.comment.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class CommentUserMismatchException extends ApplicationException {
    public CommentUserMismatchException() {
        super(CommentErrorCode.USER_MISMATCH);
    }
}
