package org.kmin.board.api.auth.domain.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class Oauth2TokenRequestException extends ApplicationException {
    public Oauth2TokenRequestException() {
        super(ErrorCode.OAUTH2_TOKEN_REQUEST_FAILED);
    }
}
