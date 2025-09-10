package org.kmin.board.api.apps.user.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }
}
