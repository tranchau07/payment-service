package com.payment.service.identity.service;

import com.payment.service.identity.entity.User;
import com.payment.service.identity.repository.InvalidatedTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityJwtService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${identity.jwt.signer-key}")
    private String signerKey;

    @Value("${identity.jwt.valid-duration:3600}")
    private long validDuration; // in seconds

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(signerKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        SecretKey key = getSigningKey();
        
        String scope = buildScope(user);
        String jti = UUID.randomUUID().toString();
        
        return Jwts.builder()
                .subject(user.getUsername())
                .id(jti)
                .claim("scope", scope)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validDuration * 1000))
                .signWith(key, Jwts.SIG.HS512) // Use HS512 algorithm as requested
                .compact();
    }

    public Claims extractAllClaims(String token) {
        SecretKey key = getSigningKey();
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractJti(String token) {
        return extractAllClaims(token).getId();
    }

    public String extractScope(String token) {
        return extractAllClaims(token).get("scope", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String jti = claims.getId();
            
            // Check if token is in blacklist
            if (invalidatedTokenRepository.existsById(jti)) {
                log.warn("Token with JTI {} is blacklisted", jti);
                return false;
            }
            
            // Check expiration
            boolean expired = claims.getExpiration().before(new Date());
            if (expired) {
                log.warn("Token with JTI {} is expired", jti);
                return false;
            }
            
            return true;
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }

        return stringJoiner.toString();
    }
}
