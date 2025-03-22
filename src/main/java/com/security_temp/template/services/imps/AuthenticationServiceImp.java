package com.security_temp.template.services.imps;

import java.util.logging.Logger;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.security_temp.template.dtos.mapper.UserDTO;
import com.security_temp.template.dtos.request.LoginRequest;
import com.security_temp.template.dtos.response.AuthenticationResponse;
import com.security_temp.template.models.User;
import com.security_temp.template.repositories.UserRepository;
import com.security_temp.template.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

    private final Logger logger = Logger.getLogger(AuthenticationServiceImp.class.getName());

    private final JwtServiceImp jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse login(LoginRequest loginDTO) {
        logger.warning(
                "authenticated user: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()));

        String username = loginDTO.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(username))
                .userDto(UserDTO.toUserDTO(user))
                .build();
    }
}
