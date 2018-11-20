package com.kawika.smart_survey.models;
/*
 * Created by akhil on 20/02/18.
 */

import java.util.List;

public class Categories {

    /**
     * status : 6000
     * message : Get lists
     * data : [{"en_category_id":2,"en_category_name":"Php",
     * "attachment_url":"fe0c30193b0e082815e96cf42f4a83f3.png"},{"en_category_id":8,"en_category_name":".net","attachment_url":"d010d876864c03e6ae0a909be92fd888.png"},{"en_category_id":12,"en_category_name":"ios","attachment_url":"08591ad5952941c92ce20c01742f5e2c.jpg"},{"en_category_id":5,"en_category_name":"laravel","attachment_url":"1a3a2a5a16273a08b16c7cf5a0062418.png"},{"en_category_id":11,"en_category_name":"Android","attachment_url":"a248462056dc058d9200717162e82a57.png"}]
     */

    private int status;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * en_category_id : 2
         * en_category_name : Php
         * attachment_url : fe0c30193b0e082815e96cf42f4a83f3.png
         */

        private int en_category_id;
        private String category_color;
        private String category_name;
        private String attachment_url;
        private String image_path;

        public DataBean(String name, String url) {
            this.category_name=name;
            this.image_path=url;
        }

        public DataBean(int en_category_id, String category_color, String category_name, String image_path) {
            this.en_category_id=en_category_id;
            this.category_color=category_color;
            this.category_name=category_name;
            this.image_path=image_path;
        }

        public int getEn_category_id() {
            return en_category_id;
        }

        public void setEn_category_id(int en_category_id) {
            this.en_category_id = en_category_id;
        }

        public String getCategory_color() {
            return category_color;
        }

        public void setCategory_color(String category_color) {
            this.category_color = category_color;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getAttachment_url() {
            return attachment_url;
        }

        public void setAttachment_url(String attachment_url) {
            this.attachment_url = attachment_url;
        }
        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }
    }
}
