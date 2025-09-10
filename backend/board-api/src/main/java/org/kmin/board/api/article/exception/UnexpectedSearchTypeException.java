package org.kmin.board.api.article.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class UnexpectedSearchTypeException extends ApplicationException {
    public UnexpectedSearchTypeException() {
        super(ArticleErrorCode.UNEXPECTED_SEARCH_TYPE);
    }
}
