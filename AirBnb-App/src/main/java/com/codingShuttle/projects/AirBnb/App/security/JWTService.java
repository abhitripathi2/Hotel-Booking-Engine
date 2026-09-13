package com.codingShuttle.projects.AirBnb.App.security;

import com.codingShuttle.projects.AirBnb.App.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        // Implementation for generating access token
        return Jwts.builder()
                .subject(user.getUser_id().toString())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10)) // 10 minutes expiration
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        // Implementation for generating refresh token
        return Jwts.builder()
                .subject(user.getUser_id().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L *60*60*24*30*6)) // 6-month expiration
                .signWith(getSecretKey())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        // Implementation for extracting user ID from
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());

    }




}
