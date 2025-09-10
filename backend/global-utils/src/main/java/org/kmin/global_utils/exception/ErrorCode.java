package org.kmin.global_utils.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getMessage();
    HttpStatus getHttpStatus();
}
