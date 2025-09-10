package org.kmin.board.api.apps.auth.oauth2.application.exception;

import org.kmin.board.api.common.exception.ApplicationException;

public class Oauth2ProviderNotProvideException extends ApplicationException {
    public Oauth2ProviderNotProvideException() {
        super(OAuth2ClientErrorCode.OAUTH2_PROVIDER_NOT_PROVIDE);
    }
}
