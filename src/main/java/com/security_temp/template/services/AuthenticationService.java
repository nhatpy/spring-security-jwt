package com.security_temp.template.services;

import org.springframework.stereotype.Service;

import com.security_temp.template.dtos.request.LoginRequest;
import com.security_temp.template.dtos.response.AuthenticationResponse;

@Service
public interface AuthenticationService {
    public AuthenticationResponse login(LoginRequest loginDTO);
}
