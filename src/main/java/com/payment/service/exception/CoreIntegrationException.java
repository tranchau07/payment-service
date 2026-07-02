package com.payment.service.exception;

import lombok.Getter;

@Getter
public class CoreIntegrationException extends RuntimeException {
    private final String correlationId;

    public CoreIntegrationException(String correlationId, Throwable cause) {
        super("Khong the xac dinh ket qua xu ly tai Core WAY4", cause);
        this.correlationId = correlationId;
    }
}
