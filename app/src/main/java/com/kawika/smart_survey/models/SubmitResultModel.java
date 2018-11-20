package com.kawika.smart_survey.models;
/*
 * Created by akhil on 01/03/18.
 */

import java.util.List;

public class SubmitResultModel {

    /**
     * user_id : 1
     * category_id : 3
     * level_id : 2
     * step_id : 5
     * total_mark : 250
     * scored_mark : 75
     * full_scored : true
     * result : [{"question_id":"2","answer_id":"3","result":"true / false / none"}]
     */

    private String user_id;
    private int categories_category_id;
    private int levels_level_id;
    private int steps_step_id;
    private int level_marks;
    private int scored_marks;
    private int full_scored;
    private List<CheckedResultsSqliteModel> result;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return categories_category_id;
    }

    public void setCategory_id(int categories_category_id) {
        this.categories_category_id = categories_category_id;
    }

    public int getLevel_id() {
        return levels_level_id;
    }

    public void setLevel_id(int levels_level_id) {
        this.levels_level_id = levels_level_id;
    }

    public int getStep_id() {
        return steps_step_id;
    }

    public void setStep_id(int steps_step_id) {
        this.steps_step_id = steps_step_id;
    }

    public int getTotal_mark() {
        return level_marks;
    }

    public void setTotal_mark(int level_marks) {
        this.level_marks = level_marks;
    }

    public int getScored_mark() {
        return scored_marks;
    }

    public void setScored_mark(int scored_marks) {
        this.scored_marks = scored_marks;
    }

    public int isFull_scored() {
        return full_scored;
    }

    public void setFull_scored(int full_scored) {
        this.full_scored = full_scored;
    }

    public List<CheckedResultsSqliteModel> getResult() {
        return result;
    }

    public void setResult(List<CheckedResultsSqliteModel> result) {
        this.result = result;
    }

}
