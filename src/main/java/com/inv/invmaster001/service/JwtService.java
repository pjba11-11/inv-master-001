package com.inv.invmaster001.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Overridable via the JWT_SECRET env var; this default only applies when
    // Spring isn't managing the bean (e.g. `new JwtService()` in tests) or
    // when the property/env var is unset.
    @Value("${jwt.secret:bWktc2VjcmV0LWtleS1mb3Itand0LWF1dGgtc3lzdGVtLTIwMjY=}")
    private String SECRET =
            "bWktc2VjcmV0LWtleS1mb3Itand0LWF1dGgtc3lzdGVtLTIwMjY=";

    private final long accessExpiration = 1000 * 60 * 15; // 15 min
    private final long refreshExpiration = 1000L * 60 * 60 * 24 * 7; // 7 days

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(SECRET)
        );
    }

    public String generateAccessToken(UserDetails user) {
        return generateToken(user, accessExpiration);
    }

    public String generateRefreshToken(UserDetails user) {
        return generateToken(user, refreshExpiration);
    }

    private String generateToken(UserDetails user, long expiry) {

        String role = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + expiry)
                )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();
    }

    public boolean isValid(String token) {

        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}