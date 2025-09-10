package org.kmin.board.api.user.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }
}
