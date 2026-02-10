package com.course.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkeymysecretkey";

    private static final long EXPIRATION =
            1000 * 60 * 60; // 1 hour

    // =========================
    // GENERATE TOKEN
    // =========================
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION)
                )
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // =========================
    // VALIDATE TOKEN
    // =========================
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // EXTRACT EMAIL
    // =========================
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // =========================
    // EXTRACT ROLE
    // =========================
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // =========================
    // INTERNAL CLAIMS PARSER
    // =========================
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
