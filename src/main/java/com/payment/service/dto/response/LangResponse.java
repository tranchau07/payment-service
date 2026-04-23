package com.payment.service.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LangResponse {
    String code;        // ISO 639-2 (VD: VIE, ENG)

    String code2;       // ISO 639-1 (VD: vi, en)

    String countryCode2; // country (VD: VN, US)

    String name;        // Vietnamese, English,...
}
