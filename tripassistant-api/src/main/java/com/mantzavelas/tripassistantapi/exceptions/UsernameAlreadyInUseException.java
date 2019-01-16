package com.mantzavelas.tripassistantapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UsernameAlreadyInUseException extends RuntimeException {

    public UsernameAlreadyInUseException(String message) { super(message); }

    public UsernameAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
