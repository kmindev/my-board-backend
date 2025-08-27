package com.back.auth.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

public class Oauth2UserRequestException extends ApplicationException {
    public Oauth2UserRequestException() {
        super(ErrorCode.OAUTH2_USER_REQUEST_FAILED);
    }
}
