package com.payment.service.identity.service;

import com.payment.service.identity.dto.*;
import com.payment.service.identity.entity.InvalidatedToken;
import com.payment.service.identity.entity.Role;
import com.payment.service.identity.entity.User;
import com.payment.service.identity.repository.InvalidatedTokenRepository;
import com.payment.service.identity.repository.RoleRepository;
import com.payment.service.identity.repository.UserRepository;
import com.payment.service.exception.AppException;
import com.payment.service.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final IdentityJwtService identityJwtService;

    // Use BCryptPasswordEncoder with strength = 10 as requested
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Transactional("identityTransactionManager")
    public IdentityRegisterResponse registerUser(IdentityRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null, 
                    "Username already exists", "Tên đăng nhập đã tồn tại");
        }

        User.UserBuilder userBuilder = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // BCrypt strength = 10
                .email(request.getEmail())
                .phone(request.getPhone());

        Set<Role> userRoles = new HashSet<>();
        if (request.getRoles() != null) {
            for (String roleName : request.getRoles()) {
                Role role = roleRepository.findById(roleName)
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .name(roleName)
                                .description("Auto-created role " + roleName)
                                .build()));
                userRoles.add(role);
            }
        }
        userBuilder.roles(userRoles);

        User savedUser = userRepository.save(userBuilder.build());

        return IdentityRegisterResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .message("User registered successfully")
                .build();
    }

    @Transactional("identityTransactionManager")
    public IdentityLoginResponse login(IdentityLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHENTICATION_FAILED, null,
                        "Username or password incorrect", "Tên đăng nhập hoặc mật khẩu không đúng"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED, null,
                    "Username or password incorrect", "Tên đăng nhập hoặc mật khẩu không đúng");
        }

        String token = identityJwtService.generateToken(user);

        return IdentityLoginResponse.builder()
                .token(token)
                .message("Login successful")
                .build();
    }

    @Transactional("identityTransactionManager")
    public IdentityMessageResponse logout(String authHeader) {
        try {
            String token = authHeader;
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (token == null || !identityJwtService.isTokenValid(token)) {
                throw new AppException(ErrorCode.INVALID_TOKEN, null, 
                        "Token invalid or already expired", "Token không hợp lệ hoặc đã hết hạn");
            }

            String jti = identityJwtService.extractJti(token);
            Date expiryTime = identityJwtService.extractAllClaims(token).getExpiration();
            
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();
            
            invalidatedTokenRepository.save(invalidatedToken);
            log.info("Token with JTI {} successfully invalidated", jti);

            return IdentityMessageResponse.builder()
                    .message("Logout successful")
                    .build();
            
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to logout token: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_TOKEN, null, 
                    "Failed to invalidate token: " + e.getMessage(), "Lỗi hệ thống khi đăng xuất");
        }
    }
}
