package org.kmin.board.api.auth.exception;

import org.kmin.global_utils.exception.ApplicationException;

public class Oauth2ProviderNotProvideException extends ApplicationException {
    public Oauth2ProviderNotProvideException() {
        super(AuthErrorCode.OAUTH2_PROVIDER_NOT_PROVIDE);
    }
}
