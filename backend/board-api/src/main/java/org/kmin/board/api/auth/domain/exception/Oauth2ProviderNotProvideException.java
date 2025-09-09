package org.kmin.board.api.auth.domain.exception;

import org.kmin.board.api.common.exception.ApplicationException;
import org.kmin.board.api.common.exception.ErrorCode;

public class Oauth2ProviderNotProvideException extends ApplicationException {
    public Oauth2ProviderNotProvideException() {
        super(ErrorCode.OAUTH2_PROVIDER_NOT_PROVIDE);
    }
}
