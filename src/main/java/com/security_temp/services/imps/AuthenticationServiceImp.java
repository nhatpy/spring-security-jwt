package com.security_temp.services.imps;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.security_temp.dtos.mapper.UserDTO;
import com.security_temp.dtos.request.LoginRequest;
import com.security_temp.dtos.response.AuthenticationResponse;
import com.security_temp.models.User;
import com.security_temp.repositories.UserRepository;
import com.security_temp.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {
    private final JwtServiceImp jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse login(LoginRequest loginDTO) {
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
