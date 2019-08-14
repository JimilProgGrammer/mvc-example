package com.arch.mvc.dto;

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
