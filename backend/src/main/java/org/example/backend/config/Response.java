package org.example.backend.config;

import lombok.Data;

@Data
public class Response {
    private boolean result;
    private int errorCode;
    private String message;

    public Response(boolean result, int errorCode, String message) {
        this.result = result;
        this.errorCode = errorCode;
        this.message = message;
    }

    public Response(MyException e) {
        this.result = false;
        this.errorCode = e.getCode();
        this.message = e.getMessage();
    }
}
