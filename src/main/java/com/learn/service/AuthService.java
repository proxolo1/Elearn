package com.learn.service;

import com.learn.constants.RoleEnum;
import com.learn.dto.AuthRequest;
import com.learn.dto.AuthResponse;
import com.learn.exception.EmailAlreadyExistException;
import com.learn.exception.ResourceNotFoundException;
import com.learn.model.Role;
import com.learn.model.User;
import com.learn.repo.RoleRepository;
import com.learn.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public ResponseEntity<AuthResponse>registerUser(AuthRequest request){
        if(Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))){
            logger.error("user already exist");
        throw new EmailAlreadyExistException("Email already exists");
    }
        User user = new User();
        Role role = roleRepository.findByName(RoleEnum.valueOf("ROLE_USER").name()).orElseThrow(()->new UsernameNotFoundException("ROLE_USER EXCEPTION"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getFirstName());
        user.setEmail(request.getEmail());
        user.setJobTitle(request.getJobTitle());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        logger.info("user registered");
        userRepository.save(user);
        return ResponseEntity.ok(new AuthResponse(user.getEmail()+" successfully registered",true));
    }

    public ResponseEntity<AuthResponse>loginUser(AuthRequest request){
        try{
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            logger.info("user logged in");
            return ResponseEntity.ok(new AuthResponse( jwtService.generateToken(request.getEmail()),true)) ;
        }
        catch (AuthenticationException ex){
            logger.error("invalid user credentials");
            throw new ResourceNotFoundException("User",request.getEmail(),"invalid user email or password");
        }
    }
}
