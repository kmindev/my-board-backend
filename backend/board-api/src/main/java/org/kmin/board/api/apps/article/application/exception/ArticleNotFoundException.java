package org.kmin.board.api.apps.article.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class ArticleNotFoundException extends ApplicationException {
    public ArticleNotFoundException() {
        super(ArticleErrorCode.NOT_FOUND);
    }
}
