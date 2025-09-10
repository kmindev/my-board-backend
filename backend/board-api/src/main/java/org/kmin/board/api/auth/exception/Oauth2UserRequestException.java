package org.kmin.board.api.auth.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class Oauth2UserRequestException extends ApplicationException {
    public Oauth2UserRequestException() {
        super(AuthErrorCode.OAUTH2_USER_REQUEST_FAILED);
    }
}
