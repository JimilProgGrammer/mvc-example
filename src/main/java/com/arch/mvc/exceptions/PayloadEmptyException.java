package com.arch.mvc.exceptions;

/**
 * Custom exception to indicate that the payload
 * in a POST request was null or empty.
 *
 * @author jimil
 */
public class PayloadEmptyException extends Exception {

    public PayloadEmptyException(String message) {
        super(message);
    }

}
