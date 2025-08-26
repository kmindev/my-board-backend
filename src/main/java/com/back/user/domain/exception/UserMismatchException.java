package com.back.user.domain.exception;

import com.back.common.exception.ApplicationException;
import com.back.common.exception.ErrorCode;

public class UserMismatchException extends ApplicationException {
    public UserMismatchException() {
        super(ErrorCode.USER_MISMATCH);
    }
}
