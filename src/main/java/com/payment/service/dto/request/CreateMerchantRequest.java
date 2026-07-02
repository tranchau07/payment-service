package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMerchantRequest {
    @NotBlank @Size(max = 255) String reason;
    @NotBlank @Size(max = 4) String institutionCode;
    @NotBlank @Size(max = 32) String branchCode;
    @NotBlank @Pattern(regexp = "C") String clientCategory;
    @NotBlank @Size(max = 32) String clientTypeCode;
    @NotBlank @Pattern(regexp = "M") String productCategory;
    @NotBlank @Size(max = 255) String companyName;
    String tradeName;
    @NotBlank @Size(max = 32) String shortName;
    String url;
    @NotBlank @Size(max = 3) String languageCode;
    String phone;
    String mobilePhone;
    @Email @Size(max = 255) String email;
    @NotBlank @Size(max = 32) String clientNumber;
    @NotBlank @Size(max = 32) String registrationType;
    @NotBlank @Size(max = 255) String registrationNumber;
    String registrationDetails;
    @NotBlank @Size(max = 32) String tin;
    @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String registrationDate;
}
