package com.security_temp.services.imps;

import java.text.ParseException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.security_temp.services.JwtService;

@Service
public class JwtServiceImp implements JwtService {
    private final String jwtSceret = "12756749edd3782ab3215057d492b0bd7895bfef5829d0c0b0df90f3618ca52e";
    private final long jwtExpiration = 3600000;

    @Override
    public String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpiration))
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claims);

        try {
            JWSSigner signer = new MACSigner(jwtSceret.getBytes());
            signedJWT.sign(signer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signedJWT.serialize();
    }

    @Override
    public boolean verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(jwtSceret.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (expirationTime.before(new Date())) {
            return false;
        }
        return signedJWT.verify(verifier);
    }

    @Override
    public String getUsernameFromToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }
}
