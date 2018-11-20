package com.kawika.smart_survey.models;

/**
 * Created by senthiljs on 23/02/18.
 */

public class BasicRetroCallModel {

    /**
     * status : 6000
     * message : User Categories created
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
