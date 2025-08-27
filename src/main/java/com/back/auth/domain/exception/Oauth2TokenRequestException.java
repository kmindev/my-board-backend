package com.back.auth.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

public class Oauth2TokenRequestException extends ApplicationException {
    public Oauth2TokenRequestException() {
        super(ErrorCode.OAUTH2_TOKEN_REQUEST_FAILED);
    }
}
