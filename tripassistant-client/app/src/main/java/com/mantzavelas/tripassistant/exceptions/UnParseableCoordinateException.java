package com.mantzavelas.tripassistant.exceptions;

public class UnParseableCoordinateException extends RuntimeException{

    public UnParseableCoordinateException() {
        super("Could not parse co-ordinate");
    }

    public UnParseableCoordinateException(String message) {
        super(message);
    }
}
