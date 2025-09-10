package org.kmin.board.api.apps.auth.oauth2.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class Oauth2UserRequestException extends ApplicationException {
    public Oauth2UserRequestException() {
        super(OAuth2ClientErrorCode.OAUTH2_USER_REQUEST_FAILED);
    }
}
