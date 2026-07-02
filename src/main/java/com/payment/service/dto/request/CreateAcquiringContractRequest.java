package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAcquiringContractRequest {
    @NotBlank @Pattern(regexp = "CLIENT_ID")
    String clientSearchMethod;
    @NotBlank
    String clientIdentifier;
    @NotBlank
    String productCode;
    String productCode2;
    String productCode3;
    @Valid
    @NotNull
    InObject inObject;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class InObject {
        @NotBlank @Size(max = 4) String institutionCode;
        @NotBlank @Size(max = 32) String branch;
        @Size(max = 255) String contractNumber;
        @NotBlank @Size(max = 255) String contractName;
        @NotBlank @Size(min = 3, max = 3) String currency;
        @NotBlank @Size(max = 4) String mcc;
        @NotBlank @Size(max = 14) @Pattern(regexp = "[A-Za-z0-9]+") String merchantId;
        @NotBlank @Size(max = 64) String cbsNumber;
        @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String openDate;
    }

}
