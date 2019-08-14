package com.arch.mvc.exceptions;

/**
 * Custom exception to indicate invalid username
 * and password combination.
 *
 * @author jimil
 */
public class IncorrectCredentialsException extends Exception {

    public IncorrectCredentialsException(String message) {
        super(message);
    }

}
