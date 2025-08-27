package com.back.article.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

public class ArticleNotFoundException extends ApplicationException {
    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
