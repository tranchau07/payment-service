package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMerchantRequest {
    String reason;
    String institutionCode;
    String branchCode;
    String clientCategory;
    String clientTypeCode;
    String productCategory;
    String companyName;
    String tradeName;
    String shortName;
    String url;
    String languageCode;
    String phone;
    String mobilePhone;
    String email;
    String clientNumber;
    String registrationType;
    String registrationNumber;
    String registrationDetails;
    String tin;
    String registrationDate;
}
