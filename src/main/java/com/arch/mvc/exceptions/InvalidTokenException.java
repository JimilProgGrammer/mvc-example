package com.arch.mvc.exceptions;

/**
 * Custom exception to indicate invalid token in header.
 *
 * @author jimil
 */
public class InvalidTokenException extends Exception {

    public InvalidTokenException(String message) {
        super(message);
    }

}
