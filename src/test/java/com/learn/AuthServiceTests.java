//package com.learn;
//
//import com.learn.constants.RoleEnum;
//import com.learn.dto.AuthRequest;
//import com.learn.dto.AuthResponse;
//import com.learn.dto.JwtResponse;
//import com.learn.exception.EmailAlreadyExistException;
//import com.learn.exception.ResourceNotFoundException;
//import com.learn.model.Role;
//import com.learn.model.User;
//import com.learn.repo.RoleRepository;
//import com.learn.repo.UserRepository;
//import com.learn.service.AuthService;
//import com.learn.service.JwtService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import javax.management.relation.RoleNotFoundException;
//import java.lang.reflect.InvocationTargetException;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(classes = {AuthServiceTests.class})
//class AuthServiceTests {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private RoleRepository roleRepository;
//    @InjectMocks
//    private AuthService authService;
//    @Mock
//    private PasswordEncoder passwordEncoder;
//    @Mock
//    private AuthenticationManager authenticationManager;
//    @Mock
//    private JwtService jwtService;
//    @Test
//
//    void test_registerUser() throws Exception {
//        AuthRequest request = new AuthRequest();
//        request.setFirstName("John");
//        request.setLastName("Doe");
//        request.setEmail("johndoe@example.com");
//        request.setJobTitle("Software Engineer");
//        request.setPhoneNumber("1234567890");
//        request.setPassword("Test@12345");
//        Role role = new Role();
//        role.setName(RoleEnum.ROLE_USER.name());
//        when(roleRepository.findByName(RoleEnum.ROLE_USER.name())).thenReturn(Optional.of(role));
//        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
//        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_password");
//
//        ResponseEntity<AuthResponse> response = authService.registerUser(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("johndoe@example.com successfully registered", Objects.requireNonNull(response.getBody()).getMessage());
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//    @Test
//    void testNullRequest() throws Exception {
//        assertThrows(IllegalArgumentException.class,()->{
//            authService.registerUser(null);
//        });
//
//    }
//    @Test
//    void testInvalidEmail()throws Exception{
//        AuthRequest request = new AuthRequest();
//        request.setFirstName("John");
//        request.setLastName("Doe");
//        request.setEmail("invalid email");
//        request.setJobTitle("Software Engineer");
//        request.setPhoneNumber("1234567890");
//        request.setPassword("Test@12345");
//        assertThrows(IllegalArgumentException.class,()->{
//            authService.registerUser(request);
//        });
//    }
//    @Test
//     void testInvalidPhoneNumber() throws Exception {
//        AuthRequest request = new AuthRequest();
//        request.setFirstName("John");
//        request.setLastName("Doe");
//        request.setEmail("johndoe@example.com");
//        request.setJobTitle("Software Engineer");
//        request.setPhoneNumber("invalid phone number");
//        request.setPassword("Test@12345");
//        assertThrows(IllegalArgumentException.class,()->{
//            authService.registerUser(request);
//        });
//    }
//    @Test
//     void testInvalidPassword() throws Exception {
//        AuthRequest request = new AuthRequest();
//        request.setFirstName("John");
//        request.setLastName("Doe");
//        request.setEmail("johndoe@example.com");
//        request.setJobTitle("Software Engineer");
//        request.setPhoneNumber("1234567890");
//        request.setPassword("Test");
//        assertThrows(IllegalArgumentException.class,()->{
//            authService.registerUser(request);
//        });
//
//    }
//    @Test
//     void testEmailAlreadyExists() throws Exception {
//        AuthRequest request = new AuthRequest();
//        request.setFirstName("John");
//        request.setLastName("Doe");
//        request.setEmail("johndoe@example.com");
//        request.setJobTitle("Software Engineer");
//        request.setPhoneNumber("1234567890");
//        request.setPassword("Test@12345");
//        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
//        assertThrows(EmailAlreadyExistException.class,()->{
//            authService.registerUser(request);
//        });
//
//    }
//   @Test
//   void testForSuccessfulLogin() throws Exception {
//       String email = "test@email.com";
//       String password = "Password1@";
//       String role = "ROLE_ADMIN";
//
//       User user = new User();
//       user.setEmail(email);
//       user.setPassword(passwordEncoder.encode(password));
//       Role userRole = new Role();
//       userRole.setName(role);
//       user.setRole(userRole);
//
//       when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//       when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))).thenReturn(null);
//       when(jwtService.generateToken(email)).thenReturn("token");
//
//       ResponseEntity<JwtResponse> response = authService.loginUser(email, password);
//
//       assertEquals(HttpStatus.OK, response.getStatusCode());
//       assertEquals(email, Objects.requireNonNull(response.getBody()).getEmail());
//       assertEquals(role, response.getBody().getRole());
//       Assertions.assertNotNull(response.getBody().getToken());
//   }
//    @Test
//    void testForIllegalArgumentException() {
//        String email = "";
//        String password = "";
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            authService.loginUser(email, password);
//        });
//    }
//    @Test
//    void testForAuthenticationException() {
//        String email = "test@email.com";
//        String password = "Password1@";
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//        assertThrows(NoSuchElementException.class, () -> {
//            authService.loginUser(email, password);
//        });
//    }
//}
