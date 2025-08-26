package com.back.auth.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

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
