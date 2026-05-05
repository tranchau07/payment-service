package com.payment.service.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientTypeResponse {
    String code;

    String name;

    Long financialInstitutionId;

    String productCategory;

    String clientCategory;

    String residence;
}
