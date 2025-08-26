package com.back.auth.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

public class Oauth2ProviderNotProvideException extends ApplicationException {
    public Oauth2ProviderNotProvideException() {
        super(ErrorCode.OAUTH2_PROVIDER_NOT_PROVIDE);
    }
}
