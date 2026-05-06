package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyResponse {
    Long id;
    String name;
    String code;
    String numericCode;
    String isActive;
}
