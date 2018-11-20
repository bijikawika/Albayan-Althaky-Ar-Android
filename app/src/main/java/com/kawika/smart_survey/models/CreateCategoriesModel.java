package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 09/03/18.
 */

public class CreateCategoriesModel {

    /**
     * status : 6000
     * message : User Categories created
     * data : [{"en_category_id":2,"category_color":"#38bedb","category_name":"Financial Regulations and Regulations of the Financial Regulations","attachment_url":"9a0883a6b6aef27b9906a39609fe06eb.png","crown":"none","crown_image":"http://e-learning.kawikatech.com/data/crown/959d9beaa70896717e3c4163067a9.png","is_followed":0,"level_count":2,"image_path":"http://e-learning.kawikatech.com/data/category/9a0883a6b6aef27b9906a39609fe06eb.png"},{"en_category_id":5,"category_color":"#665757","category_name":"Dubai HR Law","attachment_url":"b9d0fab5352a02b13110a3302814c022.png","crown":"none","crown_image":"http://e-learning.kawikatech.com/data/crown/959d9beaa70896717e3c4163067a9.png","is_followed":1,"level_count":1,"image_path":"http://e-learning.kawikatech.com/data/category/b9d0fab5352a02b13110a3302814c022.png"},{"en_category_id":8,"category_color":"#1bed56","category_name":"Dubai Corporate Law","attachment_url":"30fc5e5ab99462ad706df8eade5ef670.png","crown":"no crown","crown_image":"You dont have crwon","is_followed":1,"level_count":0,"image_path":"http://e-learning.kawikatech.com/data/category/30fc5e5ab99462ad706df8eade5ef670.png"},{"en_category_id":11,"category_color":"#2a31e0","category_name":"Dubai Financial and Contract Law","attachment_url":"b51959d9beaa70896717e3c4163067a9.png","crown":"no crown","crown_image":"You dont have crwon","is_followed":1,"level_count":0,"image_path":"http://e-learning.kawikatech.com/data/category/b51959d9beaa70896717e3c4163067a9.png"}]
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
         * category_color : #38bedb
         * category_name : Financial Regulations and Regulations of the Financial Regulations
         * attachment_url : 9a0883a6b6aef27b9906a39609fe06eb.png
         * crown : none
         * crown_image : http://e-learning.kawikatech.com/data/crown/959d9beaa70896717e3c4163067a9.png
         * is_followed : 0
         * level_count : 2
         * image_path : http://e-learning.kawikatech.com/data/category/9a0883a6b6aef27b9906a39609fe06eb.png
         */

        private int en_category_id;
        private String category_color;
        private String category_name;
        private String attachment_url;
        private String crown;
        private String crown_image;
        private int is_followed;
        private int level_count;
        private String image_path;

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

        public String getCrown() {
            return crown;
        }

        public void setCrown(String crown) {
            this.crown = crown;
        }

        public String getCrown_image() {
            return crown_image;
        }

        public void setCrown_image(String crown_image) {
            this.crown_image = crown_image;
        }

        public int getIs_followed() {
            return is_followed;
        }

        public void setIs_followed(int is_followed) {
            this.is_followed = is_followed;
        }

        public int getLevel_count() {
            return level_count;
        }

        public void setLevel_count(int level_count) {
            this.level_count = level_count;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }
    }
}
