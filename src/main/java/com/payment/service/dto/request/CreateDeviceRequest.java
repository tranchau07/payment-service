package com.payment.service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDeviceRequest {
    @NotBlank
    String reason;
    @NotBlank
    String contractSearchMethod;
    @NotBlank
    String contractIdentifier;
    @NotBlank
    String deviceTypeCode;
    @NotBlank
    @Pattern(regexp = "[YN]", message = "EnableImmediately chỉ nhận Y hoặc N")
    String enableImmediately;
    @NotBlank
    String productCode;
    @Valid
    @NotNull
    InObject inObject;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class InObject {
        @NotBlank
        @Size(max = 32)
        @Pattern(regexp = "[A-Za-z0-9._-]+", message = "SerialNumber contains invalid characters")
        String serialNumber;
        @NotBlank
        String defaultCurrency;
        @NotBlank
        @Pattern(regexp = "(?:[01]\\d|2[0-3])[0-5]\\d", message = "StartTime phải có định dạng HHmm")
        String startTime;
        @NotBlank
        @Pattern(regexp = "(?:[01]\\d|2[0-3])[0-5]\\d", message = "EndTime phải có định dạng HHmm")
        String endTime;
        @NotBlank
        @Pattern(regexp = "(?:[01]\\d|2[0-3])[0-5]\\d", message = "CutOffTime phải có định dạng HHmm")
        String cutOffTime;
        @NotBlank
        @Pattern(regexp = "[CRU]", message = "TransactionClass chỉ nhận C, R hoặc U")
        String transactionClass;
    }
}
