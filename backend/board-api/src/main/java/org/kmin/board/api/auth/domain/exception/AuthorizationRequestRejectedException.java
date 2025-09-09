package org.kmin.board.api.auth.domain.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class AuthorizationRequestRejectedException extends ApplicationException {

    private String error;

    public AuthorizationRequestRejectedException(String error) {
        super(ErrorCode.AUTHORIZATION_REQUEST_REJECTED);
        this.error = error;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " - error: " + error;
    }

}
