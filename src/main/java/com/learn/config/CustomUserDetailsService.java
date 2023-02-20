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


/**
 * The `CustomUserDetailsService` class implements the `UserDetailsService` interface.
 * It retrieves a user's information from the `UserRepository` and converts it into a
 * `UserDetails` object that can be used by the Spring Security framework.
 *
 * @author (Ajay k santhosh)
 * @version (v1)
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * An instance of the `UserRepository` class to retrieve user information from the database.
     */
    @Autowired
    private UserRepository repository;
    /**
     * A logger for logging messages related to this class.
     */
    final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    /**
     * Loads a user's information from the database based on their email. If the user is not found,
     * a `UsernameNotFoundException` is thrown.
     *
     * @param email The email of the user to look up.
     * @return A `UserDetails` object containing the user's information.
     * @throws UsernameNotFoundException If the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email).orElseThrow(()->{
            logger.info(String.format("User with email :'%s' not found",email));
            throw new UsernameNotFoundException(email +" not found");
        });
        logger.info("Looking up user with email: {}", email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthority(user.getRole()));
    }

    /**
     * Maps a user's role to a `GrantedAuthority` object.
     *
     * @param roles The role of the user.
     * @return A `Collection` of `GrantedAuthority` objects.
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthority(Role roles) {
        logger.info("map roles to authority");
        return Collections.singleton(new SimpleGrantedAuthority(roles.getName()));
    }
}
