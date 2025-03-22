package com.security_temp.services;

import org.springframework.stereotype.Service;

import com.security_temp.dtos.request.LoginRequest;
import com.security_temp.dtos.response.AuthenticationResponse;

@Service
public interface AuthenticationService {
    public AuthenticationResponse login(LoginRequest loginDTO);
}
