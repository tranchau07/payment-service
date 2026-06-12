package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientHierarchyResponse {
    ClientResponse client;
    List<ContractNodeResponse> contracts;
}
