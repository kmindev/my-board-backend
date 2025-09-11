package org.kmin.board.api.apps.article.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class ArticleUserMismatchException extends ApplicationException {
    public ArticleUserMismatchException() {
        super(ArticleErrorCode.USER_MISMATCH);
    }
}
