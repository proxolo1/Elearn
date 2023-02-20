package com.learn.service;

import com.learn.exception.JwtTokenExpireException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component

public class JwtService {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    /**
     * Secret key used to sign the token.
     */
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * Extracts the username from the given token.
     *
     * @param token The JWT token.
     * @return The username.
     */
    public String extractUsername(String token) {
        log.info("Extracting username from token.");
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        log.info("Extracting expiration from token.");
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts the specified claim from the given token.
     *
     * @param token         The JWT token.
     * @param claimsResolver The function to extract the claim.
     * @param <T>            The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    /**
     * Extracts all claims from a given token.
     *
     * @param token the token to extract claims from
     * @return the Claims object representing all the claims in the token
     */
    private Claims extractAllClaims(String token) {
        log.info("Extracting all claims from token.");
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * Checks if a given token has expired.
     *
     * @param token the token to check
     * @return true if the token has expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        log.info("Checking if token is expired.");
        return extractExpiration(token).before(new Date());
    }
    /**
     * Validates a given token against a UserDetails object.
     * The token is considered valid if it has not expired and the subject (username) in the token
     * matches the username in the UserDetails object.
     *
     * @param token the token to validate
     * @param userDetails the UserDetails object to validate the token against
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        log.info("Validating token.");
        final String username = extractUsername(token);
        if(Boolean.TRUE.equals(isTokenExpired(token))){
            log.error("Token has expired.");
            throw new JwtTokenExpireException("TOKEN EXPIRED");
        }
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Generates a new token for a given user.
     *
     * @param userName the username to use as the subject (username) in the token
     * @return the generated token
     */
    public String generateToken(String userName){
        log.info("Generating new token for user: {}", userName);
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userName);
    }
    /**
     * Creates a token from a given set of claims and a subject (username).
     *
     * @param claims the claims to include in the token
     * @param userName the subject (username) to include in the token
     * @return the generated token
     */
    private String createToken(Map<String, Object> claims, String userName) {
        log.info("Creating token for user: {}", userName);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+300000000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    /**
     * Returns the key used for signing.
     *
     * @return the Key object representing the signing key.
     */
    private Key getSignKey() {
        log.info("Getting sign key.");
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

