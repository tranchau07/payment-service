package com.payment.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAcquiringContractAddressRequest {
    String reason;
    @NotBlank String addressTypeCode;
    String branch;
    @NotBlank @Size(min = 3, max = 3) String country;
    @NotBlank @Size(min = 2, max = 2) String region;
    String district;
    String city;
    String zipCode;
    @NotBlank @Size(max = 255) String line1;
    String line2;
    String line3;
    String line4;
}
