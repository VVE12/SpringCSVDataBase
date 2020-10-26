package com.project.springcsvdatabase.response;

public class PriceItemResponse {

    private String message;

    public PriceItemResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
