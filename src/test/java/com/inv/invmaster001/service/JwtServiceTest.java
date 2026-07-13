package com.inv.invmaster001.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    // Must match the hardcoded secret in JwtService
    private static final String SECRET =
            "bWktc2VjcmV0LWtleS1mb3Itand0LWF1dGgtc3lzdGVtLTIwMjY=";

    private final JwtService jwtService = new JwtService();

    private final UserDetails admin = User.withUsername("a@b.com")
            .password("x")
            .roles("ADMIN")
            .build();

    @Test
    void accessTokenHasThreeSegmentsAndRoundTrips() {
        String token = jwtService.generateAccessToken(admin);

        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
        assertThat(jwtService.extractUsername(token)).isEqualTo("a@b.com");
        assertThat(jwtService.extractClaims(token).get("role")).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void freshTokenIsValid() {
        assertThat(jwtService.isValid(jwtService.generateAccessToken(admin))).isTrue();
    }

    @Test
    void garbageTokenIsInvalid() {
        assertThat(jwtService.isValid("not-a-jwt")).isFalse();
    }

    @Test
    void tamperedSignatureIsInvalid() {
        String token = jwtService.generateAccessToken(admin);
        char last = token.charAt(token.length() - 1);
        String tampered = token.substring(0, token.length() - 1) + (last == 'a' ? 'b' : 'a');

        assertThat(jwtService.isValid(tampered)).isFalse();
    }

    @Test
    void expiredTokenIsInvalid() {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
        String expired = Jwts.builder()
                .setSubject("a@b.com")
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        assertThat(jwtService.isValid(expired)).isFalse();
    }

    @Test
    void refreshTokenOutlivesAccessToken() {
        Date accessExp = jwtService
                .extractClaims(jwtService.generateAccessToken(admin))
                .getExpiration();
        Date refreshExp = jwtService
                .extractClaims(jwtService.generateRefreshToken(admin))
                .getExpiration();

        assertThat(refreshExp).isAfter(accessExp);
    }
}
