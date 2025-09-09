package org.kmin.board.api.auth.domain.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class Oauth2UserRequestException extends ApplicationException {
    public Oauth2UserRequestException() {
        super(ErrorCode.OAUTH2_USER_REQUEST_FAILED);
    }
}
