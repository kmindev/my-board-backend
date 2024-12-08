package com.back.controler.dto.reponse;

import com.back.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        int code,
        String message,
        T data
) {

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(HttpStatus.OK.value(), null, null);
    }

    public static <T> ApiResponse<T> okWithData(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), null, data);
    }

    public static <T> ApiResponse<T> okWithMessageAndData(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, data);
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getHttpStatus().value(), errorCode.getMessage(), null);
    }

    public static ApiResponse<Void> errorWithMessage(HttpStatus httpStatus, String errorMessage) {
        return new ApiResponse<>(httpStatus.value(), errorMessage, null);
    }

}
