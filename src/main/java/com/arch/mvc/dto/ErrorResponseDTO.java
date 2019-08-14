package com.arch.mvc.dto;

/**
 * Models the error key in the BaseResponseDTO.
 * has two keys:
 *   1. message - localized error message
 *   2. error - exception with stack trace.
 *
 * @author jimil
 */
public class ErrorResponseDTO {

    ErrorResponseDTO(String message, Exception e) {
        this.message = message;
        this.error = e;
    }

    private String message;
    private Exception error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }
}
