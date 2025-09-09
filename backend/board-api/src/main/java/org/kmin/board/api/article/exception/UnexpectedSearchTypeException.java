package org.kmin.board.api.article.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class UnexpectedSearchTypeException extends ApplicationException {
    public UnexpectedSearchTypeException() {
        super(ErrorCode.UNEXPECTED_SEARCH_TYPE);
    }
}
