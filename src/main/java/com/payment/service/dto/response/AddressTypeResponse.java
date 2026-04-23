package com.payment.service.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressTypeResponse {
    String name;
    String code;
    String groupCode;
    Long standbyAddressType;
    String useClientDefault;
    String addInfo;
}
