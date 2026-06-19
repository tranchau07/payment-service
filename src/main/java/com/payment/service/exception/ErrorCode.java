package com.payment.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_REQUEST_DATA("VAL_01", "Dữ liệu yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    CLIENT_NOT_FOUND("CLI_04", "Không tìm thấy thông tin khách hàng", HttpStatus.NOT_FOUND),
    CLIENT_ALREADY_EXISTS("CLI_05", "Mã khách hàng (Client Number) đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    CORE_CONNECTION_FAILED("INT_50", "Không thể kết nối đến hệ thống Core Way4", HttpStatus.SERVICE_UNAVAILABLE),
    CORE_CLIENT_CREATION_FAILED("INT_01", "Lỗi tạo khách hàng trên Core", HttpStatus.UNPROCESSABLE_ENTITY),
    CORE_CONTRACT_CREATION_FAILED("INT_02", "Lỗi tạo hợp đồng trên Core", HttpStatus.UNPROCESSABLE_ENTITY),
    CORE_CARD_CREATION_FAILED("INT_03", "Lỗi phát hành thẻ trên Core", HttpStatus.UNPROCESSABLE_ENTITY),
    INTERNAL_SYSTEM_ERROR("SYS_99", "Có lỗi hệ thống xảy ra, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),
    CONTRACT_ALREADY_EXISTS("CTR_01", "Số hợp đồng đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    MERCHANT_ID_ALREADY_EXISTS("CTR_02", "Mã MerchantID đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    CBS_NUMBER_ALREADY_EXISTS("CTR_03", "Số CBSNumber đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("PRD_01", "Sản phẩm không tồn tại hoặc không phù hợp", HttpStatus.BAD_REQUEST),
    MCC_NOT_FOUND("MCC_01", "Mã ngành nghề MCC (SIC) không tồn tại hoặc không được phép sử dụng", HttpStatus.BAD_REQUEST),

    // Security Error Codes
    AUTHENTICATION_FAILED("SEC_01", "Xác thực thất bại", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED("SEC_02", "Tài khoản bị khóa", HttpStatus.LOCKED),
    INVALID_TOKEN("SEC_03", "Token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),
    TOTP_INVALID("SEC_04", "Mã xác thực TOTP không hợp lệ", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("SEC_05", "Không có quyền truy cập tài nguyên này", HttpStatus.FORBIDDEN),
    RATE_LIMITED("SEC_06", "Quá nhiều yêu cầu, vui lòng thử lại sau", HttpStatus.TOO_MANY_REQUESTS);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
