package com.security_temp.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security_temp.dtos.request.LoginRequest;
import com.security_temp.dtos.response.AuthenticationResponse;
import com.security_temp.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginDTO) {
        return authenticationService.login(loginDTO);
    }
}
