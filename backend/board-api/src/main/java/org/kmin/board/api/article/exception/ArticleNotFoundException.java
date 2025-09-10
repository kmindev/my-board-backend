package org.kmin.board.api.article.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class ArticleNotFoundException extends ApplicationException {
    public ArticleNotFoundException() {
        super(ArticleErrorCode.NOT_FOUND);
    }
}
