package org.kmin.board.api.apps.comment.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class CommentNotFoundException extends ApplicationException {
    public CommentNotFoundException() {
        super(CommentErrorCode.COMMENT_NOT_FOUND);
    }
}
