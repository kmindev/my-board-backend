package org.kmin.board.api.apps.auth.oauth2.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class Oauth2TokenRequestException extends ApplicationException {
    public Oauth2TokenRequestException() {
        super(OAuth2ClientErrorCode.OAUTH2_TOKEN_REQUEST_FAILED);
    }
}
