package com.learn.config;

import com.learn.filter.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * SecurityConfig class contains the configuration for security of the application.
 * It uses different beans to enforce security on different endpoints and sets rules for authentication, authorization and encryption.
 *
 * @author [Author Name]
 * @since [Date]
 */
@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * This method returns the security filter chain that sets rules for authorization.
     * It uses HttpSecurity to define endpoints that are open for all or need authentication.
     * Also, it sets session management and adds filters for JWT validation and authentication provider.
     *
     * @param http HttpSecurity object to define security rules.
     * @return SecurityFilterChain object that sets security rules.
     * @throws Exception in case of any error during the configuration of security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        logger.info("calling security filter chain :{}",http);
      http.
        csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**","/swagger-ui/**","/v3/**")
                .permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/api/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilterBean(), UsernamePasswordAuthenticationFilter.class);
        http.cors();
        return http.build();
    }

    /**
     * This method returns a PasswordEncoder object for BCrypt encryption.
     *
     * @return BCryptPasswordEncoder object for password encryption.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * This method returns an AuthenticationProvider object for user authentication.
     * It uses DAO Authentication Provider to authenticate the user and set user details service and password encoder.
     *
     * @return DaoAuthenticationProvider object for user authentication.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsServiceBean());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        logger.info("calling authentication provider : {}",daoAuthenticationProvider);
        return daoAuthenticationProvider;
    }

    /**
     * This method returns the authentication manager for authentication of users.
     *
     * @param config AuthenticationConfiguration object for the authentication manager.
     * @return AuthenticationManager object for user authentication.
     * @throws Exception in case of any error during the configuration of authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public UserDetailsService customUserDetailsServiceBean(){
        return new CustomUserDetailsService();
    }
    @Bean
    public JwtFilter jwtFilterBean(){
        return new JwtFilter();
    }

}
