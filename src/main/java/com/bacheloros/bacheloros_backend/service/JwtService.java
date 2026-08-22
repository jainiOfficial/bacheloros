package com.bacheloros.bacheloros_backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Secret key — sirf server ko pata honi chahiye. Abhi hardcoded hai, baad mein config se aayegi.
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            "this-is-a-very-long-secret-key-for-bacheloros-jwt-signing-1234".getBytes()
    );

    private final long expirationMs = 1000 * 60 * 60 * 24; // 24 ghante

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // email store ho raha hai
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername());
        // (expiry check automatically ho jaata hai parsing ke waqt — agar expired hai, exception throw hoga)
    }
}