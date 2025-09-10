package org.kmin.board.api.article.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class ArticleUserMismatchException extends ApplicationException {
    public ArticleUserMismatchException() {
        super(ArticleErrorCode.USER_MISMATCH);
    }
}
