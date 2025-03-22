package com.security_temp.template.services;

import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;

@Service
public interface JwtService {
    public String generateToken(String username);

    public boolean verifyToken(String token) throws JOSEException, ParseException;

    public String getUsernameFromToken(String token) throws ParseException;
}
