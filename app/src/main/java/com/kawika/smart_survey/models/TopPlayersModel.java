package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 09/03/18.
 */

public class TopPlayersModel {

    /**
     * status : 6000
     * message : Top player lists...!!
     * data : [{"user_id":217,"total_mark":60,"rank":1,"player_details":{"user_mark_id":46,"firstname":"Karthik","lastname":"Chinnadurai","user_id":217,"total_mark":60,"image":"1f4244f85e7f7ce221bbcdea94bf0b20_1521455161.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","image_path":"http://e-learning.kawikatech.com/data/profile/1f4244f85e7f7ce221bbcdea94bf0b20_1521455161.png"}},{"user_id":229,"total_mark":20,"rank":2,"player_details":{"user_mark_id":36,"firstname":"V1","lastname":"V1","user_id":229,"total_mark":20,"image":"d7fd89f7cc9e87ce9bd7cbe14d29e666_1521198915.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","image_path":"http://e-learning.kawikatech.com/data/profile/d7fd89f7cc9e87ce9bd7cbe14d29e666_1521198915.png"}}]
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
         * user_id : 217
         * total_mark : 60
         * rank : 1
         * player_details : {"user_mark_id":46,"firstname":"Karthik","lastname":"Chinnadurai","user_id":217,"total_mark":60,"image":"1f4244f85e7f7ce221bbcdea94bf0b20_1521455161.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","image_path":"http://e-learning.kawikatech.com/data/profile/1f4244f85e7f7ce221bbcdea94bf0b20_1521455161.png"}
         */

        private int user_id;
        private int total_mark;
        private int rank;
        private PlayerDetailsBean player_details;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getTotal_mark() {
            return total_mark;
        }

        public void setTotal_mark(int total_mark) {
            this.total_mark = total_mark;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public PlayerDetailsBean getPlayer_details() {
            return player_details;
        }

        public void setPlayer_details(PlayerDetailsBean player_details) {
            this.player_details = player_details;
        }

        public static class PlayerDetailsBean {
            /**
             * user_mark_id : 46
             * firstname : Karthik
             * lastname : Chinnadurai
             * user_id : 217
             * total_mark : 60
             * image : 1f4244f85e7f7ce221bbcdea94bf0b20_1521455161.png
             * category_name : Financial Regulations and Regulations of the Financial Regulations
             * image_path : http://e-learning.kawikatech.com/data/profile/1f4244f85e7f7ce221bbcdea94bf0b20_1521455161.png
             */

            private int user_mark_id;
            private String firstname;
            private String lastname;
            private int user_id;
            private int total_mark;
            private String image;
            private String category_name;
            private String image_path;

            public int getUser_mark_id() {
                return user_mark_id;
            }

            public void setUser_mark_id(int user_mark_id) {
                this.user_mark_id = user_mark_id;
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

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getTotal_mark() {
                return total_mark;
            }

            public void setTotal_mark(int total_mark) {
                this.total_mark = total_mark;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
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
        }
    }
}
