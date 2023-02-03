package com.learn.config;

import com.learn.model.Role;
import com.learn.model.User;
import com.learn.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;


@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email).orElseThrow(()->{
            logger.info(String.format("User with email :'%s' not found",email));
            throw new UsernameNotFoundException(email +" not found");
        });
        logger.info("Looking up user with email: {}", email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthority(user.getRole()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthority(Role roles) {
        logger.info("map roles to authority");
        return Collections.singleton(new SimpleGrantedAuthority(roles.getName()));
    }
}
