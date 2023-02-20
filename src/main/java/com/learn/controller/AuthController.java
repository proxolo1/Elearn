package com.learn.controller;

import com.learn.dto.AuthRequest;
import com.learn.dto.AuthResponse;
import com.learn.dto.JwtResponse;
import com.learn.dto.LoginDto;
import com.learn.model.User;
import com.learn.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
/**

 The AuthController class is used to handle requests related to user authentication.

 @author [Your Name]

 @version [Version]

 @since [YYYY-MM-DD]
 **/

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    /**
     * Logger instance to log the events
     */
    final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * Autowired instance of AuthService
     */
    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     * @param request object containing information about the user to be registered
     * @return ResponseEntity with AuthResponse as body
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @PostMapping("register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest request) throws Exception {
        logger.info("Received a request to the endpoint '/register' : {}",request);
        return authService.registerUser(request);
    }

    /**
     * Login a user
     * @param loginDto object containing email and password of the user trying to login
     * @return ResponseEntity with JwtResponse as body
     */
    @PostMapping("login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginDto loginDto){
        logger.info("Received a request to the endpoint '/login' : email : {}, password :{}",loginDto.getEmail(),loginDto.getPassword());
        return authService.loginUser(loginDto.getEmail(),loginDto.getPassword());
    }

    /**
     * Get the user information
     * @param email of the user to be fetched
     * @return ResponseEntity with User as body
     */
    @GetMapping("user")
    public ResponseEntity<User>getUser(@RequestParam("email") String email){
        return authService.getUser(email);
    }

}
