package com.HopeConnect.HC.controllers;


import com.HopeConnect.HC.payload.authentication.AuthenticationPayload;
import com.HopeConnect.HC.payload.authentication.RegisterPayload;
import com.HopeConnect.HC.response.AuthenticationResponse;
import com.HopeConnect.HC.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/HopeConnect/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterPayload request){


        return ResponseEntity.ok(authenticationService.register(request));


    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationPayload request){

        return ResponseEntity.ok(authenticationService.authenticate(request));



    }
}