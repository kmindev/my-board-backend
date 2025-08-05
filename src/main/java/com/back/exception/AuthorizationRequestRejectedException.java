package com.back.exception;

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
