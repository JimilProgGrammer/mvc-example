package com.arch.mvc.dto;

/**
 * This DTO is used as a response in all the APIs.
 * has two keys:
 *   1. data - on successful execution of API, data key holds
 *   the requested data or an acknowledgment.
 *   2. error- in case of exception, error key holds the
 *   exception details.
 *
 * @author jimil
 */
public class BaseResponseDTO {

    private Object data;
    private ErrorResponseDTO error;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ErrorResponseDTO getError() {
        return error;
    }

    public void setError(String message, Exception e) {
        this.error = new ErrorResponseDTO(message, e);
    }
}
