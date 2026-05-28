package com.payment.service.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String correlationId;
    private final Object details;

    public AppException(ErrorCode errorCode, String correlationId, Object details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.correlationId = correlationId;
        this.details = details;
    }

    public AppException(ErrorCode errorCode, String correlationId, String customMessage, Object details) {
        super(customMessage);
        this.errorCode = errorCode;
        this.correlationId = correlationId;
        this.details = details;
    }
}
