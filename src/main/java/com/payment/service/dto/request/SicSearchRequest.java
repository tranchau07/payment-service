package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SicSearchRequest {
    String code;
    String name;
    String groupCode;
    String customCode;
    String useInBank;
}
