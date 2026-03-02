package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.dto.LoginRequest;
import com.marceloneuro.internalfintech.dto.LoginResponse;
import com.marceloneuro.internalfintech.security.CustomUserDetails;
import com.marceloneuro.internalfintech.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication auth = authenticationManager.authenticate(usernamePasswordToken);

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        String token = tokenService.generateToken(userDetails);

        return new LoginResponse(token);
    }
}
