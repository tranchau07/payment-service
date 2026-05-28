package com.payment.service.security.service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Slf4j
@Service
public class TotpService {

    private final boolean totpEnabled;

    public TotpService(@Value("${security.totp.enabled:false}") boolean totpEnabled) {
        this.totpEnabled = totpEnabled;
    }

    public boolean isTotpEnabled() {
        return totpEnabled;
    }

    public String generateSecret() {
        return new DefaultSecretGenerator(32).generate();
    }

    public String generateQrCodeBase64(String secret, String username) {
        try {
            QrData data = new QrData.Builder()
                    .label(username)
                    .secret(secret)
                    .issuer("PaymentCardSystem")
                    .algorithm(HashingAlgorithm.SHA1)
                    .digits(6)
                    .period(30)
                    .build();

            ZxingPngQrGenerator generator = new ZxingPngQrGenerator();
            byte[] imageData = generator.generate(data);
            return Base64.getEncoder().encodeToString(imageData);
        } catch (Exception e) {
            log.error("Failed to generate QR code for user: {}", username, e);
            throw new RuntimeException("Failed to generate TOTP QR code", e);
        }
    }

    public boolean verifyCode(String secret, String code) {
        if (!totpEnabled || code == null || code.isBlank()) {
            return !totpEnabled;
        }
        try {
            CodeVerifier verifier = new DefaultCodeVerifier(
                    new DefaultCodeGenerator(),
                    new SystemTimeProvider()
            );
            return verifier.isValidCode(secret, code);
        } catch (Exception e) {
            log.warn("TOTP verification error: {}", e.getMessage());
            return false;
        }
    }
}
