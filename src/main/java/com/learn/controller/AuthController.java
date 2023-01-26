package com.learn.controller;

import com.learn.dto.AuthRequest;
import com.learn.dto.AuthResponse;
import com.learn.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest request) {

        return authService.registerUser(request);
    }
    @PostMapping("login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request){
       return authService.loginUser(request);
    }
}
