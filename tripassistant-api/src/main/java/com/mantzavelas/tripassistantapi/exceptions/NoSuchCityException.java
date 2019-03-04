package com.mantzavelas.tripassistantapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class NoSuchCityException extends RuntimeException {

    public NoSuchCityException() {
        super("Requested city is not supported in the system yet!");
    }

    public NoSuchCityException(String message) {
        super(message);
    }
}
