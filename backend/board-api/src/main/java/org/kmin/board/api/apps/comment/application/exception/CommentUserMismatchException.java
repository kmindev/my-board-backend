package org.kmin.board.api.apps.comment.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class CommentUserMismatchException extends ApplicationException {
    public CommentUserMismatchException() {
        super(CommentErrorCode.USER_MISMATCH);
    }
}
