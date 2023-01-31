package com.learn.controller;

import com.learn.dto.AuthRequest;
import com.learn.dto.AuthResponse;
import com.learn.dto.JwtResponse;
import com.learn.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.lang.reflect.InvocationTargetException;

@CrossOrigin(origins="http://localhost:4200/")

@RestController
@RequestMapping("/auth")
public class AuthController {
    final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @PostMapping("register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest request) throws IllegalAccessException, InvocationTargetException {
        logger.info("Received a request to the endpoint '/register' : {}",request);
        return authService.registerUser(request);
    }
    @PostMapping("login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody AuthRequest request){
        logger.info("Received a request to the endpoint '/login' : {}",request);
        return authService.loginUser(request);
    }
}
