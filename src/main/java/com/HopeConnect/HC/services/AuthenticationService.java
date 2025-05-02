package com.HopeConnect.HC.services;

import com.HopeConnect.HC.models.User.Role;
import com.HopeConnect.HC.models.User.User;
import com.HopeConnect.HC.payload.authentication.AuthenticationPayload;
import com.HopeConnect.HC.payload.authentication.RegisterPayload;
import com.HopeConnect.HC.repositories.UserRepository;
import com.HopeConnect.HC.response.AuthenticationResponse;
import com.HopeConnect.HC.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

private UserRepository userRepository;
private PasswordEncoder passwordEncoder;
private  JwtService jwtService;
private  AuthenticationManager authenticationManager;
    public com.HopeConnect.HC.response.AuthenticationResponse register(RegisterPayload request) {

        var user= User.builder()
                .userName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();
        userRepository.save(user);

        var jwtToken= jwtService.generateToken(user);
        return com.HopeConnect.HC.response.AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationPayload request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken= jwtService.generateToken(user);
        return com.HopeConnect.HC.response.AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
}
