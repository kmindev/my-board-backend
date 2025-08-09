package com.back.exception;

public class Oauth2ProviderNotProvideException extends ApplicationException{
    public Oauth2ProviderNotProvideException() {
        super(ErrorCode.OAUTH2_PROVIDER_NOT_PROVIDE);
    }
}
