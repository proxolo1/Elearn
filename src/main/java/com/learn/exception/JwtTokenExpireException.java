package com.learn.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class JwtTokenExpireException extends JwtException {
    public JwtTokenExpireException(String message) {
        super(message);
    }
}
