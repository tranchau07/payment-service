package com.payment.service.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phone;
    private Set<String> roles;
}
