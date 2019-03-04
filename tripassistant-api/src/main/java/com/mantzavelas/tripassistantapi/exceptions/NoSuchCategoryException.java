package com.mantzavelas.tripassistantapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class NoSuchCategoryException extends RuntimeException{

	public NoSuchCategoryException() { }

	public NoSuchCategoryException(String category) { super("Category " + category + " does not exist on system yet!"); }
}
