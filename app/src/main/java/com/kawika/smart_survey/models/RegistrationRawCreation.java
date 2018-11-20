package com.kawika.smart_survey.models;

/**
 * Created by senthiljs on 01/02/18.
 */

public class RegistrationRawCreation {

    private String firstname;
    private String lastname;
    private String profile_image;
    private String email;
    private String phone;
    private String password;
    private int languages_language_id;
    private int departments_department_id;
    private long device_id;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setLanguages_language_id(int languages_language_id) {
        this.languages_language_id = languages_language_id;
    }
    public void setDepartments_department_id(int departments_department_id) {
        this.departments_department_id = departments_department_id;
    }
    public void setDevice_id(long device_id) {
        this.device_id = device_id;
    }
}
