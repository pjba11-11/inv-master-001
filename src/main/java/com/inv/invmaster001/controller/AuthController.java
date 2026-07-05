package com.inv.invmaster001.controller;

import com.inv.invmaster001.dto.*;
import com.inv.invmaster001.service.AuthService;
import com.inv.invmaster001.service.CustomUserDetailsService;
import com.inv.invmaster001.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private AuthService authService;

    // LOGIN
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email,
                        request.password
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.email);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    // REFRESH
    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody RefreshRequest request) {

        if (!jwtService.isValid(request.refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(request.refreshToken);

        UserDetails user = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    @PostMapping("/user/register")
    public ResponseEntity<RegisterCommonResponse> register(@RequestBody RegisterUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerUser(request));
    }

    @PostMapping("/company/register")
    public ResponseEntity<RegisterCommonResponse> registerCompany(@RequestBody RegisterCompanyRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerCompany(request));
    }


}