package com.back.article.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

public class UnexpectedSearchTypeException extends ApplicationException {
    public UnexpectedSearchTypeException() {
        super(ErrorCode.UNEXPECTED_SEARCH_TYPE);
    }
}
