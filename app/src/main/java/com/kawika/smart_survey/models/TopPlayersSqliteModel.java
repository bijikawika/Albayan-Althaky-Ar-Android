package com.kawika.smart_survey.models;

/**
 * Created by senthiljs on 10/03/18.
 */

public class TopPlayersSqliteModel {
    private int en_category_id;
    private String firstname;
    private String lastname;
    private int user_id;
    private int total_mark;
    private int rank;
    private int user_result_id;
    private String image;
    private String category_name;

    public int getEn_category_id() {
        return en_category_id;
    }

    public void setEn_category_id(int en_category_id) {
        this.en_category_id = en_category_id;
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
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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
}
