package com.security_temp.template.configs;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.security_temp.template.models.User;
import com.security_temp.template.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("useradmin").isPresent()) {
                return;
            }
            if (userRepository.findByUsername("usernormal").isPresent()) {
                return;
            }
            if (userRepository.findByUsername("userall").isPresent()) {
                return;
            }

            User userall = User.builder()
                    .username("userall")
                    .password(passwordEncoder.encode("000000"))
                    .authorities(List.of(
                            new SimpleGrantedAuthority("ROLE_ADMIN"),
                            new SimpleGrantedAuthority("ROLE_USER")))
                    .build();

            User useradmin = User.builder()
                    .username("useradmin")
                    .password(passwordEncoder.encode("000000"))
                    .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();

            User usernormal = User.builder()
                    .username("usernormal")
                    .password(passwordEncoder.encode("000000"))
                    .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
                    .build();

            userRepository.save(useradmin);
            userRepository.save(usernormal);
            userRepository.save(userall);
        };
    }
}
