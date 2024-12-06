package com.back.controler.advice;

import com.back.dto.reponse.ApiResponse;
import com.back.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionRestAdvice {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> applicationException(ApplicationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> dbException(DataAccessException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "디비 에러!"));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> serverException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러!"));
    }

}
