package org.kmin.board.api.apps.auth.oauth2.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class AuthorizationRequestRejectedException extends ApplicationException {

    private String error;

    public AuthorizationRequestRejectedException(String error) {
        super(OAuth2ClientErrorCode.AUTHORIZATION_REQUEST_REJECTED);
        this.error = error;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " - error: " + error;
    }

}
