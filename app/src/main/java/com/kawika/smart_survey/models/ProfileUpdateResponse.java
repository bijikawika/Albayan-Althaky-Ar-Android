package com.kawika.smart_survey.models;
/*
 * Created by akhil on 08/03/18.
 */

public class ProfileUpdateResponse {


    /**
     * status : 6000
     * message : Profile Successfully Updated
     * data : {"image_path":"http://e-learning.kawikatech.com/data/profile/090786baf90f08444cb6e189f305d402_1520494453.png","firstname":"Akhil chandran"}
     */

    private int status;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * image_path : http://e-learning.kawikatech.com/data/profile/090786baf90f08444cb6e189f305d402_1520494453.png
         * firstname : Akhil chandran
         */

        private String image_path;
        private String firstname;

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }
    }
}
