package com.jwt.demo.bean;

import java.io.Serializable;

public class HttpResponseBean implements Serializable {

    private String httpStatus;
    private String message;

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
