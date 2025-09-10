package org.kmin.board.api.auth.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class AuthorizationRequestRejectedException extends ApplicationException {

    private String error;

    public AuthorizationRequestRejectedException(String error) {
        super(AuthErrorCode.AUTHORIZATION_REQUEST_REJECTED);
        this.error = error;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " - error: " + error;
    }

}
