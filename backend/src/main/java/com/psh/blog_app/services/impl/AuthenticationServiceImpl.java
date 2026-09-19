package com.psh.blog_app.services.impl;

import com.psh.blog_app.services.AuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final Long jwtExpiryMs = 86400000L;



    @Override
    public UserDetails authenticate(String email, String password) {
        // authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
                                          );

        return userDetailsService.loadUserByUsername(email);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return  Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ jwtExpiryMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSigningKey(){
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
