package com.kawika.smart_survey.models;
/*
 * Created by akhil on 27/02/18.
 */

public class CommonModel {

    /**
     * status : 6000
     * message : Forgot password OTP success
     * user_id : 1
     */

    private int status;
    private String message;
    private int user_id;

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
