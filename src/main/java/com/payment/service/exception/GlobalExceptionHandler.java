package com.payment.service.exception;

import com.payment.service.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        log.error("[AppException] ErrorCode: {}, CorrelationID: {}, Details: {}", 
                ex.getErrorCode().getCode(), ex.getCorrelationId(), ex.getDetails(), ex);
        
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .correlationId(ex.getCorrelationId())
                .details(ex.getDetails())
                .build();
                
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("[ValidationException] Details: {}", errors);

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_REQUEST_DATA.getCode())
                .message(ErrorCode.INVALID_REQUEST_DATA.getMessage())
                .timestamp(LocalDateTime.now())
                .details(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_REQUEST_DATA.getCode())
                .message("Thiáº¿u HTTP header báº¯t buá»™c: " + ex.getHeaderName())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("[BusinessException] Message: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("BUS_01")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
        log.warn("[SecurityException] Access denied: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ErrorCode.ACCESS_DENIED.getCode())
                .message(ErrorCode.ACCESS_DENIED.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("[GeneralException] Uncaught error: ", ex);

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ErrorCode.INTERNAL_SYSTEM_ERROR.getCode())
                .message(ErrorCode.INTERNAL_SYSTEM_ERROR.getMessage())
                .timestamp(LocalDateTime.now())
                .details(ex.getMessage()) // Trả về thông tin debug nội bộ (chỉ nên dùng trong DEV/Staging)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
