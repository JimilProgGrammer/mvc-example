package com.arch.mvc.exceptions;

/**
 * Custom exception class to indicate that no tweets were found
 * for the given query.
 *
 * @author jimil
 */
public class TweetsNotFoundException extends Exception {

    public TweetsNotFoundException(String message) {
        super(message);
    }

}
