package org.kmin.board.api.user.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class UserMismatchException extends ApplicationException {
    public UserMismatchException() {
        super(ErrorCode.USER_MISMATCH);
    }
}
