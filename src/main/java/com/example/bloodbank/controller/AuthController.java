package com.example.bloodbank.controller;

import com.example.bloodbank.models.AuthReq;
import com.example.bloodbank.models.AuthResp;
import com.example.bloodbank.models.ForgotPasswordRequest;
import com.example.bloodbank.models.ResetPasswordRequest;
import com.example.bloodbank.proxy.UserProxy;
import com.example.bloodbank.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserProxy userProxy){
        System.out.println(userProxy);
        String register = authService.register(userProxy);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResp> login(@RequestBody AuthReq authReq){
        AuthResp resp = authService.login(authReq);
        if (resp.getToken() == null) {
            // locked or failed — return 200 with message so Angular can read it cleanly
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/forget")
    public ResponseEntity<String> forget(@RequestBody AuthReq authReq){
        System.out.println(authReq);
        String forget = authService.forget(authReq);
        return ResponseEntity.ok(forget);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request){
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request){
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}
