package com.arch.mvc.exceptions;

/**
 * Custom Exception to indicate that the user is already
 * registered.
 *
 * @author jimil
 */
public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
