package org.kmin.board.api.common.exception;

import org.kmin.board.api.common.presentation.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionRestAdvice {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleRestClientException(RestClientException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(ApiResponse.errorWithMessage(HttpStatus.BAD_GATEWAY, "외부 서비스 호출에 실패했습니다."));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.errorWithMessage(
                            HttpStatus.BAD_REQUEST,
                            mismatchedInputException.getPath().get(0).getFieldName() + " 필드의 값이 잘못되었습니다.")
                    );
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.errorWithMessage(HttpStatus.BAD_REQUEST, "확인할 수 없는 형태의 데이터가 들어왔습니다"));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleMissingPathVariableExceptions(MissingPathVariableException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.errorWithMessage(HttpStatus.BAD_REQUEST, "요청 경로가 올바르지 않습니다."));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleMethoArgumentTypeMismatchExceptions(
            MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        String msg = String.format("요청 파라미터 '%s' 는 유효하지 않습니다.", e.getValue());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.errorWithMessage(HttpStatus.BAD_REQUEST, msg));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.errorWithMessage(HttpStatus.BAD_REQUEST,
                        e.getBindingResult().getFieldError().getDefaultMessage()));
    }

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
