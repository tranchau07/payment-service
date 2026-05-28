package com.payment.service.security.service;

import com.payment.service.dto.request.LoginRequest;
import com.payment.service.dto.response.LoginResponse;
import com.payment.service.exception.AppException;
import com.payment.service.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TotpService totpService;

    // In-memory brute-force protection
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockedAccounts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    // In-memory refresh token store (token -> username)
    private final Set<String> validRefreshTokens = ConcurrentHashMap.newKeySet();

    // TOTP secrets per user (populated by config)
    private final Map<String, String> totpSecrets = new ConcurrentHashMap<>();

    public AuthService(UserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       TotpService totpService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.totpService = totpService;
    }

    @PostConstruct
    public void initTotpSecrets() {
        if (totpService.isTotpEnabled()) {
            registerTotpSecret("admin", "JBSWY3DPEHPK3PXP");
            registerTotpSecret("supervisor", "GEZDGNBVGY3TQOJQ");
            registerTotpSecret("teller01", "MEZDENRVGY3TQOJQ");
        }
    }

    public void registerTotpSecret(String username, String secret) {
        totpSecrets.put(username, secret);
    }

    public String getTotpSecret(String username) {
        return totpSecrets.get(username);
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();

        // Check account lock
        if (isAccountLocked(username)) {
            log.warn("[SECURITY] Login attempt on locked account: {}", username);
            throw new AppException(ErrorCode.ACCOUNT_LOCKED, null,
                    "Tài khoản bị khóa do đăng nhập sai nhiều lần. Vui lòng thử lại sau " + LOCK_MINUTES + " phút.", null);
        }

        // Load user
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            recordFailedAttempt(username);
            log.warn("[SECURITY] Login failed - user not found: {}", username);
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED, null, "Tên đăng nhập hoặc mật khẩu không đúng", null);
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            recordFailedAttempt(username);
            log.warn("[SECURITY] Login failed - wrong password for user: {}", username);
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED, null, "Tên đăng nhập hoặc mật khẩu không đúng", null);
        }

        // Verify TOTP if enabled
        if (totpService.isTotpEnabled()) {
            String totpSecret = totpSecrets.get(username);
            if (totpSecret != null) {
                if (request.getTotpCode() == null || request.getTotpCode().isBlank()) {
                    // Return response indicating TOTP is required
                    return LoginResponse.builder()
                            .totpRequired(true)
                            .message("Vui lòng nhập mã xác thực từ Google Authenticator")
                            .build();
                }
                if (!totpService.verifyCode(totpSecret, request.getTotpCode())) {
                    log.warn("[SECURITY] TOTP verification failed for user: {}", username);
                    throw new AppException(ErrorCode.TOTP_INVALID, null, "Mã xác thực không hợp lệ hoặc đã hết hạn", null);
                }
            }
        }

        // Success: reset failed attempts
        resetFailedAttempts(username);

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        validRefreshTokens.add(refreshToken);

        log.info("[SECURITY] Login successful for user: {}", username);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(username)
                .roles(userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toList()))
                .totpRequired(false)
                .message("Đăng nhập thành công")
                .build();
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken == null || !validRefreshTokens.contains(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_TOKEN, null, "Refresh token không hợp lệ hoặc đã bị thu hồi", null);
        }

        try {
            String username = jwtService.extractUsername(refreshToken);
            String tokenType = jwtService.extractTokenType(refreshToken);

            if (!"REFRESH".equals(tokenType)) {
                throw new AppException(ErrorCode.INVALID_TOKEN, null, "Token không phải refresh token", null);
            }

            if (jwtService.isTokenExpired(refreshToken)) {
                validRefreshTokens.remove(refreshToken);
                throw new AppException(ErrorCode.INVALID_TOKEN, null, "Refresh token đã hết hạn", null);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Token rotation: invalidate old, issue new
            validRefreshTokens.remove(refreshToken);
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            validRefreshTokens.add(newRefreshToken);

            log.info("[SECURITY] Token refreshed for user: {}", username);

            return LoginResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .username(username)
                    .roles(userDetails.getAuthorities().stream()
                            .map(a -> a.getAuthority().replace("ROLE_", ""))
                            .collect(Collectors.toList()))
                    .totpRequired(false)
                    .message("Token đã được làm mới")
                    .build();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            validRefreshTokens.remove(refreshToken);
            throw new AppException(ErrorCode.INVALID_TOKEN, null, "Không thể làm mới token", null);
        }
    }

    public void logout(String refreshToken) {
        if (refreshToken != null) {
            validRefreshTokens.remove(refreshToken);
            log.info("[SECURITY] Refresh token revoked on logout");
        }
    }

    private void recordFailedAttempt(String username) {
        int attempts = failedAttempts.merge(username, 1, Integer::sum);
        if (attempts >= MAX_ATTEMPTS) {
            lockedAccounts.put(username, LocalDateTime.now().plusMinutes(LOCK_MINUTES));
            log.warn("[SECURITY] Account locked due to {} failed attempts: {}", attempts, username);
        }
    }

    private void resetFailedAttempts(String username) {
        failedAttempts.remove(username);
        lockedAccounts.remove(username);
    }

    private boolean isAccountLocked(String username) {
        LocalDateTime lockedUntil = lockedAccounts.get(username);
        if (lockedUntil == null) return false;
        if (LocalDateTime.now().isAfter(lockedUntil)) {
            lockedAccounts.remove(username);
            failedAttempts.remove(username);
            return false;
        }
        return true;
    }
}
