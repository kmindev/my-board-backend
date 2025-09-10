package org.kmin.board.api.comment.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class CommentNotFoundException extends ApplicationException {
    public CommentNotFoundException() {
        super(CommentErrorCode.COMMENT_NOT_FOUND);
    }
}
