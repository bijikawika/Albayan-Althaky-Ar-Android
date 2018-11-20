package com.kawika.smart_survey.models;
/*
 * Created by akhil on 23/02/18.
 */

import java.util.List;

public class AvatarModel {

    /**
     * status : 6000
     * message : User Avatar
     * data : [{"avatar_id":1,"avatar":"fe0c30193b0e082815e96cf42f4a83f3.png","created_at":"2018-02-23 06:42:33","updated_at":null,"image_path":"http://e-learning.kawikatech.com/data/avatar/fe0c30193b0e082815e96cf42f4a83f3.png"},{"avatar_id":2,"avatar":"d010d876864c03e6ae0a909be92fd888.png","created_at":"2018-02-23 06:45:34","updated_at":null,"image_path":"http://e-learning.kawikatech.com/data/avatar/d010d876864c03e6ae0a909be92fd888.png"},{"avatar_id":3,"avatar":"08591ad5952941c92ce20c01742f5e2c.jpg","created_at":"2018-02-23 06:45:50","updated_at":null,"image_path":"http://e-learning.kawikatech.com/data/avatar/08591ad5952941c92ce20c01742f5e2c.jpg"},{"avatar_id":4,"avatar":"1a3a2a5a16273a08b16c7cf5a0062418.png","created_at":"2018-02-23 06:46:16","updated_at":null,"image_path":"http://e-learning.kawikatech.com/data/avatar/1a3a2a5a16273a08b16c7cf5a0062418.png"},{"avatar_id":5,"avatar":"a248462056dc058d9200717162e82a57.png","created_at":"2018-02-23 06:47:05","updated_at":null,"image_path":"http://e-learning.kawikatech.com/data/avatar/a248462056dc058d9200717162e82a57.png"}]
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
         * avatar_id : 1
         * avatar : fe0c30193b0e082815e96cf42f4a83f3.png
         * created_at : 2018-02-23 06:42:33
         * updated_at : null
         * image_path : http://e-learning.kawikatech.com/data/avatar/fe0c30193b0e082815e96cf42f4a83f3.png
         */

        private int avatar_id;
        private String avatar;
        private String created_at;
        private Object updated_at;
        private String image_path;

        public int getAvatar_id() {
            return avatar_id;
        }

        public void setAvatar_id(int avatar_id) {
            this.avatar_id = avatar_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public Object getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(Object updated_at) {
            this.updated_at = updated_at;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }
    }
}
