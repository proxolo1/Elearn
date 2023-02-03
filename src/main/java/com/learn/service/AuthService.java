package com.learn.service;

import com.learn.constants.RoleEnum;
import com.learn.dto.AuthRequest;
import com.learn.dto.AuthResponse;
import com.learn.dto.JwtResponse;
import com.learn.exception.EmailAlreadyExistException;
import com.learn.exception.ResourceNotFoundException;
import com.learn.model.Role;
import com.learn.model.User;
import com.learn.repo.RoleRepository;
import com.learn.repo.UserRepository;
import com.learn.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JwtService jwtService,
                       AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     *
     * @param request the authentication request containing user information
     * @return a response entity with a success message and status
     * @throws EmailAlreadyExistException if the email already exists in the system
     */
    public ResponseEntity<AuthResponse> registerUser(AuthRequest request) throws IllegalAccessException, InvocationTargetException {
        // check if request is empty
        if (ValidationUtil.isBlank(request)) {
            logger.error("request can't be null");
            throw new IllegalArgumentException("fields cannot be empty");
        }
        if(!ValidationUtil.validateEmail(request.getEmail())){
            logger.error("Invalid email format: {}", request.getEmail());
            throw new IllegalArgumentException("Invalid email format");
        }
        //check if phone number is valid
        if (!ValidationUtil.validatePhoneNumber(request.getPhoneNumber())) {
            logger.error("Invalid phone number format: {}", request.getPhoneNumber());
            throw new IllegalArgumentException("Invalid phone number format");
        }
  /*
            Must contain at least one lowercase letter.
Must contain at least one uppercase letter.
Must contain at least one digit.
Must contain at least one special character (@, $, !, %, *, ?, &).
Must be at least 8 characters long.
*/
        if (!ValidationUtil.validatePassword(request.getPassword())) {
            logger.error("Invalid password format: {}", request.getPassword());
            throw new IllegalArgumentException("""
                    Must contain at least one lowercase letter
                   Must contain at least one uppercase letter
                   Must contain at least one digit
                   Must contain at least one special character (@, $, !, %, *, ?, &)
                   Must be at least 8 characters long
                    """);
        }
        // Check if email already exists
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            logger.error("User with email {} already exists", request.getEmail());
            throw new EmailAlreadyExistException("Email already exists");
        }
        // Validate phone number
        Role role = roleRepository.findByName(RoleEnum.ROLE_USER.name()).orElseThrow(() -> new ResourceNotFoundException("Role", "Role ROLE_USER not found"));
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setJobTitle(request.getJobTitle());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        logger.info("User {} successfully registered", request.getEmail());

        return ResponseEntity.ok(new AuthResponse(user.getEmail() + " successfully registered", true));
    }

    /**
     * Logs in a user.
     *
     * @param email password the authentication request containing user email and password
     * @return a response entity with a JWT token and success status
     * @throws ResourceNotFoundException if the user email or password is invalid
     */
    public ResponseEntity<JwtResponse> loginUser(String email,String password) {
        try {
            if(!ValidationUtil.isBlank(email,password)){
                logger.error("Invalid email or password format: {} , {}",email,password);
                throw new IllegalArgumentException("email or password invalid");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            logger.info("User logged in: {}",email);
            return ResponseEntity.ok(new JwtResponse(jwtService.generateToken(email), email, true));
        } catch (AuthenticationException ex) {
            logger.error("Invalid credentials for email: {}",email);
            throw new ResourceNotFoundException("User", email, "Invalid email or password");
        }
    }
    public ResponseEntity<User>getUser(String email){
        return ResponseEntity.ok(userRepository.findByEmail(email).orElseThrow());
    }
}
