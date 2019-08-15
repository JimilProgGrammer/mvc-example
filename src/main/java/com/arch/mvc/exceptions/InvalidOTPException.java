package com.arch.mvc.exceptions;

/**
 * This custom exception class indicates that the
 * OTP entered during email verification was wrong.
 *
 * @author jimil
 */
public class InvalidOTPException extends Exception {

    public InvalidOTPException(String message) {
        super(message);
    }

}
