package com.jsjg73.lmun.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyParticipationException extends RuntimeException{
    public AlreadyParticipationException() {
    }

    public AlreadyParticipationException(String message) {
        super(message);
    }
}
