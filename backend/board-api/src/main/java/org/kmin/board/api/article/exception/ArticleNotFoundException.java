package org.kmin.board.api.article.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class ArticleNotFoundException extends ApplicationException {
    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
