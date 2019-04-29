package com.mantzavelas.tripassistantapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EmptyResourceFieldException extends RuntimeException {

	public EmptyResourceFieldException(String message) {
		super(message);
	}
}
