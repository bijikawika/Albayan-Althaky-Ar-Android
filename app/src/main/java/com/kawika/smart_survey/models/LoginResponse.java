package com.kawika.smart_survey.models;
/*
 * Created by akhil on 20/02/18.
 */

public class LoginResponse {

    /**
     * status : 6000
     * message : Success
     * data : {"id":26,"firstname":"quiz_type_select_activity","lastname":"quiz_type_select_activity","username":null,"email":"test1@gmail.com","password":"$2y$10$tszJdkYyw6lZI2Go2wJMH.01AMLQIKc/0V7.5Tmdf.5CLIl0gg7ke","phone":"+9199527310178","image":"noImage_1519203170.png","device_id":"dfff","roles_role_id":2,"push_token":null,"is_active":1,"remember_token":null,"created_at":"2018-02-21 08:52:50","updated_at":"2018-02-21 09:22:02","languages_language_id":1,"departments_department_id":null}
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
         * id : 26
         * firstname : quiz_type_select_activity
         * lastname : quiz_type_select_activity
         * username : null
         * email : test1@gmail.com
         * password : $2y$10$tszJdkYyw6lZI2Go2wJMH.01AMLQIKc/0V7.5Tmdf.5CLIl0gg7ke
         * phone : +9199527310178
         * image : noImage_1519203170.png
         * device_id : dfff
         * roles_role_id : 2
         * push_token : null
         * is_active : 1
         * remember_token : null
         * created_at : 2018-02-21 08:52:50
         * updated_at : 2018-02-21 09:22:02
         * languages_language_id : 1
         * departments_department_id : null
         */

        private int id;
        private String firstname;
        private String lastname;
        private Object username;
        private String email;
        private String password;
        private String phone;
        private String image;
        private String device_id;
        private int roles_role_id;
        private Object push_token;
        private int is_active;
        private Object remember_token;
        private String created_at;
        private String updated_at;
        private int languages_language_id;
        private Object departments_department_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public int getRoles_role_id() {
            return roles_role_id;
        }

        public void setRoles_role_id(int roles_role_id) {
            this.roles_role_id = roles_role_id;
        }

        public Object getPush_token() {
            return push_token;
        }

        public void setPush_token(Object push_token) {
            this.push_token = push_token;
        }

        public int getIs_active() {
            return is_active;
        }

        public void setIs_active(int is_active) {
            this.is_active = is_active;
        }

        public Object getRemember_token() {
            return remember_token;
        }

        public void setRemember_token(Object remember_token) {
            this.remember_token = remember_token;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getLanguages_language_id() {
            return languages_language_id;
        }

        public void setLanguages_language_id(int languages_language_id) {
            this.languages_language_id = languages_language_id;
        }

        public Object getDepartments_department_id() {
            return departments_department_id;
        }

        public void setDepartments_department_id(Object departments_department_id) {
            this.departments_department_id = departments_department_id;
        }
    }
}
