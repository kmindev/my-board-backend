package com.back.exception;

public class Oauth2TokenRequestException extends ApplicationException {
    public Oauth2TokenRequestException() {
        super(ErrorCode.OAUTH2_TOKEN_REQUEST_FAILED);
    }
}
