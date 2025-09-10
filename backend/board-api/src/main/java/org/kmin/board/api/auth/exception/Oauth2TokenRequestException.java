package org.kmin.board.api.auth.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class Oauth2TokenRequestException extends ApplicationException {
    public Oauth2TokenRequestException() {
        super(AuthErrorCode.OAUTH2_TOKEN_REQUEST_FAILED);
    }
}
