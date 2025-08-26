package com.back.user.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
