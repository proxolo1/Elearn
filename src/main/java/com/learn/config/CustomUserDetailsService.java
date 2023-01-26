package com.learn.config;

import com.learn.model.Role;
import com.learn.model.User;
import com.learn.repo.UserRepository;
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
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(email +" not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthority(user.getRole()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthority(Role roles) {
        return Collections.singleton(new SimpleGrantedAuthority(roles.getName()));
    }
}
