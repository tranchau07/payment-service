package com.payment.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // InMemory users for development/internal use
        var admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN", "SUPERVISOR", "TELLER")
                .build();

        var supervisor = User.builder()
                .username("supervisor")
                .password(encoder.encode("super123"))
                .roles("SUPERVISOR", "TELLER")
                .build();

        var teller = User.builder()
                .username("teller01")
                .password(encoder.encode("teller123"))
                .roles("TELLER")
                .build();

        return new InMemoryUserDetailsManager(admin, supervisor, teller);
    }
}
