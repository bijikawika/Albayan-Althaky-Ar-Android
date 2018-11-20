package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 23/02/18.
 */

public class CheckedResultsSqliteModel {

    private int questions_question_id;
    private int answers_answer_id;
    private String is_checked;

    public int getQuestion_id() {
        return questions_question_id;
    }

    public void setQuestion_id(int questions_question_id) {
        this.questions_question_id = questions_question_id;
    }

    public int getClicked_id() {
        return answers_answer_id;
    }

    public void setClicked_id(int answers_answer_id) {
        this.answers_answer_id = answers_answer_id;
    }

    public String getPassed_result() {
        return is_checked;
    }

    public void setPassed_result(String is_checked) {
        this.is_checked = is_checked;
    }
}
