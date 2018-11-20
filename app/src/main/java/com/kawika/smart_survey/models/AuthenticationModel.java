package com.kawika.smart_survey.models;

/*
 * Created by senthiljs on 01/02/18.
 */

import java.util.List;

public class AuthenticationModel {

    /**
     * status : 6000
     * message : Success
     * data : {"id":185,"image":"082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png","department_id":4,"department":"Accounting and Finance.","device_id":"$2y$10$IhQUVGRJTe/HjN8MeLEwfeZgjboBl4wi0HlZAqnKDKZtKzQ/s9HpC","email":"Senthiljs.1992@gmail.com","firstname":"One man army","lastname":"js","language_id":1,"language":"English","phone":"+9719952730178","user_mark":0,"image_path":"http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png","challengeNotification":{"push_enable":1,"is_available":1,"message":"Your Challenge is available today"},"user_categories":[{"en_category_id":2,"category_color":"#38bedb","category_name":"Financial Regulations and Regulations of the Financial Regulations","attachment_url":"9a0883a6b6aef27b9906a39609fe06eb.png","crown":"none","crown_image":"http://e-learning.kawikatech.com/data/crown/959d9beaa70896717e3c4163067a9.png","is_followed":1,"level_count":3,"image_path":"http://e-learning.kawikatech.com/data/category/9a0883a6b6aef27b9906a39609fe06eb.png","top_players":[{"firstname":"One man army","lastname":"js","user_id":185,"total_mark":35,"user_result_id":124,"image":"082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","rank":1,"image_path":"http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png"},{"firstname":"One man army","lastname":"js","user_id":185,"total_mark":20,"user_result_id":122,"image":"082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","rank":2,"image_path":"http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png"}]},{"en_category_id":5,"category_color":"#665757","category_name":"Dubai HR Law","attachment_url":"b9d0fab5352a02b13110a3302814c022.png","crown":"no crown","crown_image":"You dont have crwon","is_followed":1,"level_count":0,"image_path":"http://e-learning.kawikatech.com/data/category/b9d0fab5352a02b13110a3302814c022.png","top_players":[]},{"en_category_id":8,"category_color":"#1bed56","category_name":"Dubai Corporate Law","attachment_url":"30fc5e5ab99462ad706df8eade5ef670.png","crown":"no crown","crown_image":"You dont have crwon","is_followed":1,"level_count":0,"image_path":"http://e-learning.kawikatech.com/data/category/30fc5e5ab99462ad706df8eade5ef670.png","top_players":[{"firstname":"Karthik","lastname":"Chinnadurai","user_id":217,"total_mark":60,"user_result_id":126,"image":"a3c55eac54202e4d62e2b96f7ad13296_1521183397.png","category_name":"Dubai Corporate Law","rank":1,"image_path":"http://e-learning.kawikatech.com/data/profile/a3c55eac54202e4d62e2b96f7ad13296_1521183397.png"},{"firstname":"Karthik","lastname":"Chinnadurai","user_id":217,"total_mark":60,"user_result_id":138,"image":"a3c55eac54202e4d62e2b96f7ad13296_1521183397.png","category_name":"Dubai Corporate Law","rank":2,"image_path":"http://e-learning.kawikatech.com/data/profile/a3c55eac54202e4d62e2b96f7ad13296_1521183397.png"}]},{"en_category_id":11,"category_color":"#2a31e0","category_name":"Dubai Financial and Contract Law","attachment_url":"b51959d9beaa70896717e3c4163067a9.png","crown":"no crown","crown_image":"You dont have crwon","is_followed":1,"level_count":0,"image_path":"http://e-learning.kawikatech.com/data/category/b51959d9beaa70896717e3c4163067a9.png","top_players":[]}]}
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
         * id : 185
         * image : 082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png
         * department_id : 4
         * department : Accounting and Finance.
         * device_id : $2y$10$IhQUVGRJTe/HjN8MeLEwfeZgjboBl4wi0HlZAqnKDKZtKzQ/s9HpC
         * email : Senthiljs.1992@gmail.com
         * firstname : One man army
         * lastname : js
         * language_id : 1
         * language : English
         * phone : +9719952730178
         * user_mark : 0
         * image_path : http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png
         * challengeNotification : {"push_enable":1,"is_available":1,"message":"Your Challenge is available today"}
         * user_categories : [{"en_category_id":2,"category_color":"#38bedb","category_name":"Financial Regulations and Regulations of the Financial Regulations","attachment_url":"9a0883a6b6aef27b9906a39609fe06eb.png","crown":"none","crown_image":"http://e-learning.kawikatech.com/data/crown/959d9beaa70896717e3c4163067a9.png","is_followed":1,"level_count":3,"image_path":"http://e-learning.kawikatech.com/data/category/9a0883a6b6aef27b9906a39609fe06eb.png","top_players":[{"firstname":"One man army","lastname":"js","user_id":185,"total_mark":35,"user_result_id":124,"image":"082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","rank":1,"image_path":"http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png"},{"firstname":"One man army","lastname":"js","user_id":185,"total_mark":20,"user_result_id":122,"image":"082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","rank":2,"image_path":"http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png"}]},{"en_category_id":5,"category_color":"#665757","category_name":"Dubai HR Law","attachment_url":"b9d0fab5352a02b13110a3302814c022.png","crown":"no crown","crown_image":"You dont have crwon","is_followed":1,"level_count":0,"image_path":"http://e-learning.kawikatech.com/data/category/b9d0fab5352a02b13110a3302814c022.png","top_players":[]},{"en_category_id":8,"category_color":"#1bed56","category_name":"Dubai Corporate Law","attachment_url":"30fc5e5ab99462ad706df8eade5ef670.png","crown":"no crown","crown_image":"You dont have crwon","is_followed":1,"level_count":0,"image_path":"http://e-learning.kawikatech.com/data/category/30fc5e5ab99462ad706df8eade5ef670.png","top_players":[{"firstname":"Karthik","lastname":"Chinnadurai","user_id":217,"total_mark":60,"user_result_id":126,"image":"a3c55eac54202e4d62e2b96f7ad13296_1521183397.png","category_name":"Dubai Corporate Law","rank":1,"image_path":"http://e-learning.kawikatech.com/data/profile/a3c55eac54202e4d62e2b96f7ad13296_1521183397.png"},{"firstname":"Karthik","lastname":"Chinnadurai","user_id":217,"total_mark":60,"user_result_id":138,"image":"a3c55eac54202e4d62e2b96f7ad13296_1521183397.png","category_name":"Dubai Corporate Law","rank":2,"image_path":"http://e-learning.kawikatech.com/data/profile/a3c55eac54202e4d62e2b96f7ad13296_1521183397.png"}]},{"en_category_id":11,"category_color":"#2a31e0","category_name":"Dubai Financial and Contract Law","attachment_url":"b51959d9beaa70896717e3c4163067a9.png","crown":"no crown","crown_image":"You dont have crwon","is_followed":1,"level_count":0,"image_path":"http://e-learning.kawikatech.com/data/category/b51959d9beaa70896717e3c4163067a9.png","top_players":[]}]
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
        private int user_mark;
        private String image_path;
        private ChallengeNotificationBean challengeNotification;
        private List<UserCategoriesBean> user_categories;

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

        public int getUser_mark() {
            return user_mark;
        }

        public void setUser_mark(int user_mark) {
            this.user_mark = user_mark;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public ChallengeNotificationBean getChallengeNotification() {
            return challengeNotification;
        }

        public void setChallengeNotification(ChallengeNotificationBean challengeNotification) {
            this.challengeNotification = challengeNotification;
        }

        public List<UserCategoriesBean> getUser_categories() {
            return user_categories;
        }

        public void setUser_categories(List<UserCategoriesBean> user_categories) {
            this.user_categories = user_categories;
        }

        public static class ChallengeNotificationBean {
            /**
             * push_enable : 1
             * is_available : 1
             * message : Your Challenge is available today
             */

            private int push_enable;
            private int is_available;
            private String message;

            public int getPush_enable() {
                return push_enable;
            }

            public void setPush_enable(int push_enable) {
                this.push_enable = push_enable;
            }

            public int getIs_available() {
                return is_available;
            }

            public void setIs_available(int is_available) {
                this.is_available = is_available;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }

        public static class UserCategoriesBean {
            /**
             * en_category_id : 2
             * category_color : #38bedb
             * category_name : Financial Regulations and Regulations of the Financial Regulations
             * attachment_url : 9a0883a6b6aef27b9906a39609fe06eb.png
             * crown : none
             * crown_image : http://e-learning.kawikatech.com/data/crown/959d9beaa70896717e3c4163067a9.png
             * is_followed : 1
             * level_count : 3
             * image_path : http://e-learning.kawikatech.com/data/category/9a0883a6b6aef27b9906a39609fe06eb.png
             * top_players : [{"firstname":"One man army","lastname":"js","user_id":185,"total_mark":35,"user_result_id":124,"image":"082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","rank":1,"image_path":"http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png"},{"firstname":"One man army","lastname":"js","user_id":185,"total_mark":20,"user_result_id":122,"image":"082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png","category_name":"Financial Regulations and Regulations of the Financial Regulations","rank":2,"image_path":"http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png"}]
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
            private List<TopPlayersBean> top_players;

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

            public List<TopPlayersBean> getTop_players() {
                return top_players;
            }

            public void setTop_players(List<TopPlayersBean> top_players) {
                this.top_players = top_players;
            }

            public static class TopPlayersBean {
                /**
                 * firstname : One man army
                 * lastname : js
                 * user_id : 185
                 * total_mark : 35
                 * user_result_id : 124
                 * image : 082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png
                 * category_name : Financial Regulations and Regulations of the Financial Regulations
                 * rank : 1
                 * image_path : http://e-learning.kawikatech.com/data/profile/082f24b877b0d2b1daa721e1ccdcb0ec_1520851786.png
                 */

                private String firstname;
                private String lastname;
                private int user_id;
                private int total_mark;
                private int user_result_id;
                private String image;
                private String category_name;
                private int rank;
                private String image_path;

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

                public int getUser_result_id() {
                    return user_result_id;
                }

                public void setUser_result_id(int user_result_id) {
                    this.user_result_id = user_result_id;
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

                public int getRank() {
                    return rank;
                }

                public void setRank(int rank) {
                    this.rank = rank;
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
}
