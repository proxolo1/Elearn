package com.learn;

import com.learn.dto.AuthRequest;
import com.learn.dto.AuthResponse;
import com.learn.exception.EmailAlreadyExistException;
import com.learn.model.Role;
import com.learn.model.User;
import com.learn.repo.RoleRepository;
import com.learn.repo.UserRepository;
import com.learn.service.AuthService;
import com.learn.service.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.RoleNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AuthServiceTests.class})
class AuthServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private AuthService authService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Test

    void test_registerUser() throws InvocationTargetException, IllegalAccessException {
        List<User> users = new ArrayList<>();
        Optional<Role> role= Optional.of(new Role(1, "ROLE_USER"));
        User user=new User(1,"ajay","santhosh","ajayksanthosh.15@gmail.com","Developer","9895774705","Qwerty1@",role.get());
        AuthRequest request=new AuthRequest(user.getFirstName(),user.getLastName(),user.getEmail(),user.getJobTitle(),user.getPhoneNumber(),user.getPassword());
        String email = "demo@demo.com";
        when(passwordEncoder.encode(request.getPassword())).thenReturn(new BCryptPasswordEncoder().encode(request.getPassword()));
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(ResponseEntity.ok(new AuthResponse(user.getEmail()+" successfully registered",true)),authService.registerUser(request));
        doThrow(new EmailAlreadyExistException("some message")).when(userRepository).existsByEmail(request.getEmail());
    }


    @Test
    void test_loginUser(){
       String email="test@test.com";
       String password="Test12sa@l";
        Authentication authentication= new Authentication() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("test","test"))).thenReturn(authentication);
        when(jwtService.generateToken("test")).thenReturn("hello");
        assertEquals(HttpStatus.OK,authService.loginUser(email,password).getStatusCode());
    }
}
