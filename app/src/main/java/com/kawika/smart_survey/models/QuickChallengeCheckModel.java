package com.kawika.smart_survey.models;

/**
 * Created by senthiljs on 15/03/18.
 */

public class QuickChallengeCheckModel {

    /**
     * status : 6000
     * message : Your Challenge is available today
     * is_available : 1
     */

    private int status;
    private String message;
    private int is_available;

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

    public int getIs_available() {
        return is_available;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }
}
