package org.kmin.board.api.user.domain.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
