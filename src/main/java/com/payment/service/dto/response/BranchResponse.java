package com.payment.service.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchResponse {
    String name;

    String code;

    Long parentId;

    Long branchOid;

    Long liabContract;

    Long unit;

    Long bankClient;

    Long timeZone;

    String unitType;
}
