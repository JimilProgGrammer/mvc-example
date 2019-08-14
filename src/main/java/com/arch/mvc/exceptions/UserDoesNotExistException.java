package com.arch.mvc.exceptions;

/**
 * Custom exception to indicate that the username
 * does not exist in the database.
 *
 * @author jimil
 */
public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException(String message) {
        super(message);
    }

}
