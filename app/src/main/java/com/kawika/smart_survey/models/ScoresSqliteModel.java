package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 23/02/18.
 */

public class ScoresSqliteModel {

    private String category_name;
    private int category_id;
    private int step_id;
    private int level_id;
    private int total_mark;
    private int scored_mark;
    private int full_scored;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getStep_id() {
        return step_id;
    }

    public void setStep_id(int step_id) {
        this.step_id = step_id;
    }

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
    }

    public int getTotal_mark() {
        return total_mark;
    }

    public void setTotal_mark(int total_mark) {
        this.total_mark = total_mark;
    }

    public int getScored_mark() {
        return scored_mark;
    }

    public void setScored_mark(int scored_mark) {
        this.scored_mark = scored_mark;
    }

    public int getFull_scored() {
        return full_scored;
    }

    public void setFull_scored(int full_scored) {
        this.full_scored = full_scored;
    }

}
