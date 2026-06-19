package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SicResponse {
    Long id;
    String name;
    String code;
    String groupCode;
    String customCode;
    String limitCode;
    Long sicGroupDflt;
    String useInBank;
    String amndState;
}
