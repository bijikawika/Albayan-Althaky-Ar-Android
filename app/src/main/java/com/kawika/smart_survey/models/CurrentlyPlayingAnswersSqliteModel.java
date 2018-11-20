package com.kawika.smart_survey.models;

/**
 * Created by senthiljs on 23/02/18.
 */

public class CurrentlyPlayingAnswersSqliteModel {

    private int steps;
    private int question_id;
    private int answer_id;
    private String answer;
    private int correct_answer;

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(int correct_answer) {
        this.correct_answer = correct_answer;
    }

}
