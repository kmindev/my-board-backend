package com.back.exception;

public class Oauth2UserRequestException extends ApplicationException{
    public Oauth2UserRequestException() {
        super(ErrorCode.OAUTH2_USER_REQUEST_FAILED);
    }
}
