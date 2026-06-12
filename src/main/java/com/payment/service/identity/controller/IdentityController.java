package com.payment.service.identity.controller;

import com.payment.service.identity.dto.*;
import com.payment.service.identity.service.IdentityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/identity")
@RequiredArgsConstructor
public class IdentityController {

    private final IdentityUserService identityUserService;

    @PostMapping("/register")
    public IdentityRegisterResponse register(@RequestBody IdentityRegisterRequest request) {
        return identityUserService.registerUser(request);
    }

    @PostMapping("/login")
    public IdentityLoginResponse login(@RequestBody IdentityLoginRequest request) {
        return identityUserService.login(request);
    }

    @PostMapping("/logout")
    public IdentityMessageResponse logout(@RequestHeader("Authorization") String authHeader) {
        return identityUserService.logout(authHeader);
    }

    @GetMapping("/me")
    public IdentityProfileResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new com.payment.service.exception.AppException(
                    com.payment.service.exception.ErrorCode.AUTHENTICATION_FAILED, 
                    null, 
                    "User not authenticated", 
                    "Chưa xác thực người dùng"
            );
        }
        
        List<String> authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        return IdentityProfileResponse.builder()
                .username((String) auth.getPrincipal())
                .authorities(authorities)
                .build();
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, String> getAdminData() {
        return Map.of(
                "message", "Access granted to admin-only API",
                "data", "Sensitive admin information"
        );
    }

    @GetMapping("/user-only")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public Map<String, String> getUserData() {
        return Map.of(
                "message", "Access granted to user-only API",
                "data", "Regular user information"
        );
    }
}
