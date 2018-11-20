package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 23/02/18.
 */

public class CategoriesSqliteModel {

    private int en_category_id;
    private String category_color;
    private String category_name;
    private String image_path;
    private int is_followed;
    private String crown;
    private String crown_image;
    private int level_count;
    private List<Categories.DataBean> data;


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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(int is_followed) {
        this.is_followed = is_followed;
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

    public int getLevel_count() {
        return level_count;
    }

    public void setLevel_count(int level_count) {
        this.level_count = level_count;
    }

    public List<Categories.DataBean> getData() {
        return data;
    }

    public void setData(List<Categories.DataBean> data) {
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
        private String crown;
        private int level_count;

        public DataBean(String name, String url) {
            this.category_name = name;
            this.image_path = url;
        }

        public DataBean(int en_category_id, String category_color, String category_name, String image_path) {
            this.en_category_id = en_category_id;
            this.category_color = category_color;
            this.category_name = category_name;
            this.image_path = image_path;
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

        public String getCrown() {
            return crown;
        }

        public void setCrown(String crown) {
            this.crown = crown;
        }

        public int getLevel_count() {
            return level_count;
        }

        public void setLevel_count(int level_count) {
            this.level_count = level_count;
        }
    }
}
