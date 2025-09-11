package org.kmin.board.api.apps.article.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class UnexpectedSearchTypeException extends ApplicationException {
    public UnexpectedSearchTypeException() {
        super(ArticleErrorCode.UNEXPECTED_SEARCH_TYPE);
    }
}
