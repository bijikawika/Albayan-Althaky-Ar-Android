package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 06/03/18.
 */

public class SubmitResultsModel {

    /**
     * status : 6000
     * message : Successfully submited
     * data : {"id":138,"image":"53bff80b7144d9914a454bf627406da7_1519824255.png","department_id":3,"department":"Research and Development ","device_id":"$2y$10$THCT8z1BNsoU3fb3OyHgCORPiFFhFSBLDGmO8m0jViQU26WCb.f8q","email":"Senthiljs.1992@gmail.com","firstname":"test","lastname":"test","language_id":1,"language":"English","phone":"+919952730178","image_path":"http://e-learning.kawikatech.com/data/profile/53bff80b7144d9914a454bf627406da7_1519824255.png","user_mark":55,"played":[{"step_id":1,"steps":1,"level_id":1,"en_category_id":2,"step_complete":1},{"step_id":2,"steps":2,"level_id":1,"en_category_id":2,"step_complete":1}],"category_id":2,"level_id":1,"step_id":1}
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
         * id : 138
         * image : 53bff80b7144d9914a454bf627406da7_1519824255.png
         * department_id : 3
         * department : Research and Development
         * device_id : $2y$10$THCT8z1BNsoU3fb3OyHgCORPiFFhFSBLDGmO8m0jViQU26WCb.f8q
         * email : Senthiljs.1992@gmail.com
         * firstname : test
         * lastname : test
         * language_id : 1
         * language : English
         * phone : +919952730178
         * image_path : http://e-learning.kawikatech.com/data/profile/53bff80b7144d9914a454bf627406da7_1519824255.png
         * user_mark : 55
         * played : [{"step_id":1,"steps":1,"level_id":1,"en_category_id":2,"step_complete":1},{"step_id":2,"steps":2,"level_id":1,"en_category_id":2,"step_complete":1}]
         * category_id : 2
         * level_id : 1
         * step_id : 1
         */

        private int id;
        private String image;
        private int department_id;
        private String department;
        private String device_id;
        private String email;
        private String firstname;
        private String lastname;
        private int language_id;
        private String language;
        private String phone;
        private String image_path;
        private int user_mark;
        private int category_id;
        private int level_id;
        private int step_id;
        private List<PlayedBean> played;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(int department_id) {
            this.department_id = department_id;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public int getLanguage_id() {
            return language_id;
        }

        public void setLanguage_id(int language_id) {
            this.language_id = language_id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public int getUser_mark() {
            return user_mark;
        }

        public void setUser_mark(int user_mark) {
            this.user_mark = user_mark;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public int getLevel_id() {
            return level_id;
        }

        public void setLevel_id(int level_id) {
            this.level_id = level_id;
        }

        public int getStep_id() {
            return step_id;
        }

        public void setStep_id(int step_id) {
            this.step_id = step_id;
        }

        public List<PlayedBean> getPlayed() {
            return played;
        }

        public void setPlayed(List<PlayedBean> played) {
            this.played = played;
        }

        public static class PlayedBean {
            /**
             * step_id : 1
             * steps : 1
             * level_id : 1
             * en_category_id : 2
             * step_complete : 1
             */

            private int step_id;
            private int steps;
            private int level_id;
            private int en_category_id;
            private int step_complete;

            public int getStep_id() {
                return step_id;
            }

            public void setStep_id(int step_id) {
                this.step_id = step_id;
            }

            public int getSteps() {
                return steps;
            }

            public void setSteps(int steps) {
                this.steps = steps;
            }

            public int getLevel_id() {
                return level_id;
            }

            public void setLevel_id(int level_id) {
                this.level_id = level_id;
            }

            public int getEn_category_id() {
                return en_category_id;
            }

            public void setEn_category_id(int en_category_id) {
                this.en_category_id = en_category_id;
            }

            public int getStep_complete() {
                return step_complete;
            }

            public void setStep_complete(int step_complete) {
                this.step_complete = step_complete;
            }
        }
    }
}
