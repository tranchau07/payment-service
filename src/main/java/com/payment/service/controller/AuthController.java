package com.payment.service.controller;

import com.payment.service.dto.request.LoginRequest;
import com.payment.service.dto.response.LoginResponse;
import com.payment.service.dto.response.TotpSetupResponse;
import com.payment.service.security.service.AuthService;
import com.payment.service.security.service.TotpService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TotpService totpService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        // If TOTP required (first step), don't set cookie
        if (loginResponse.isTotpRequired()) {
            return loginResponse;
        }

        // Set refresh token as HttpOnly cookie
        if (loginResponse.getRefreshToken() != null) {
            Cookie refreshCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false); // Set true in production with HTTPS
            refreshCookie.setPath("/api/auth");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(refreshCookie);

            // Don't send refresh token in body (it's in the cookie)
            loginResponse.setRefreshToken(null);
        }

        return loginResponse;
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        LoginResponse loginResponse = authService.refreshToken(refreshToken);

        // Set new refresh token cookie (token rotation)
        if (loginResponse.getRefreshToken() != null) {
            Cookie refreshCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false);
            refreshCookie.setPath("/api/auth");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshCookie);
            loginResponse.setRefreshToken(null);
        }

        return loginResponse;
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        authService.logout(refreshToken);

        // Clear cookie
        Cookie clearCookie = new Cookie("refreshToken", "");
        clearCookie.setHttpOnly(true);
        clearCookie.setSecure(false);
        clearCookie.setPath("/api/auth");
        clearCookie.setMaxAge(0);
        response.addCookie(clearCookie);

        return Map.of("message", "Đăng xuất thành công");
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return Map.of(
                "username", userDetails.getUsername(),
                "roles", userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/totp/setup")
    public TotpSetupResponse getTotpSetup(@RequestParam String username) {
        String secret = authService.getTotpSecret(username);
        if (secret == null) {
            secret = totpService.generateSecret();
            authService.registerTotpSecret(username, secret);
        }
        String qrCode = totpService.generateQrCodeBase64(secret, username);
        return TotpSetupResponse.builder()
                .qrCodeBase64(qrCode)
                .secret(secret)
                .username(username)
                .build();
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
