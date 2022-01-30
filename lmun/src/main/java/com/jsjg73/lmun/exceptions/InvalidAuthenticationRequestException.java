package com.jsjg73.lmun.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationRequestException extends AuthenticationException {
    public InvalidAuthenticationRequestException(String explanation) {
        super(explanation);
    }
}
