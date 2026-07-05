package com.inv.invmaster001.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // MUST be Base64 encoded secret (important fix)
    //TODO:remove hardcode;
    private final String SECRET =
            "bWktc2VjcmV0LWtleS1mb3Itand0LWF1dGgtc3lzdGVtLTIwMjY=";

    private final long accessExpiration = 1000 * 60 * 15; // 15 min
    private final long refreshExpiration = 1000L * 60 * 60 * 24 * 7; // 7 days

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    // ACCESS TOKEN
    public String generateAccessToken(UserDetails user) {
        return generateToken(user.getUsername(), accessExpiration);
    }

    // REFRESH TOKEN
    public String generateRefreshToken(UserDetails user) {
        return generateToken(user.getUsername(), refreshExpiration);
    }

    private String generateToken(String subject, long expiry) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}