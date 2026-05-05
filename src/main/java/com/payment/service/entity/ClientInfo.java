package com.payment.service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientInfo {
    String institutionCode;
    String branch;
    String clientTypeCode;
    String shortName;
    String firstName;
    String lastName;
    String middleName;
    String maritalStatusCode;
    String socialSecurityNumber;
    String salutationCode;
    String birthDate;
    String gender;
    String citizenship;
    String individualTaxpayerNumber;
    String companyName;
    String identityCardNumber;
    String identityCardDetails;
    String clientNumber;
    String profession;
    String email;
    String homePhone;
    String mobilePhone;
}
