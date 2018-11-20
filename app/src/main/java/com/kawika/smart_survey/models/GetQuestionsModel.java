package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 28/02/18.
 */

public class GetQuestionsModel {

    /**
     * status : 6000
     * message : Questions lists
     * data : [{"steps":2,"level":"Intermediate","category":"Financial Regulations and Regulations of the Financial Regulations","question":"rsujdztj","question_id":47,"explanation":"teitsreitejtr6ej","answers":[{"answer_id":296,"answer":"tseitsrite","correct_answer":0},{"answer_id":297,"answer":"setitesuijtse","correct_answer":1},{"answer_id":298,"answer":"tseidtuitdeui","correct_answer":0}]},{"steps":2,"level":"Intermediate","category":"Financial Regulations and Regulations of the Financial Regulations","question":"gfdgyhgfj","question_id":44,"explanation":"ytiutyui","answers":[{"answer_id":291,"answer":"tuyr","correct_answer":0},{"answer_id":292,"answer":"yiut","correct_answer":1},{"answer_id":293,"answer":"tyuitu","correct_answer":0}]}]
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
         * steps : 2
         * level : Intermediate
         * category : Financial Regulations and Regulations of the Financial Regulations
         * question : rsujdztj
         * question_id : 47
         * explanation : teitsreitejtr6ej
         * answers : [{"answer_id":296,"answer":"tseitsrite","correct_answer":0},{"answer_id":297,"answer":"setitesuijtse","correct_answer":1},{"answer_id":298,"answer":"tseidtuitdeui","correct_answer":0}]
         */

        private int steps;
        private String level;
        private int en_category_id;
        private String category;
        private String question;
        private int question_id;
        private String explanation;
        private List<AnswersBean> answers;

        public int getSteps() {
            return steps;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCategory() {
            return category;
        }

        public int getEn_category_id() {
            return en_category_id;
        }

        public void setEn_category_id(int en_category_id) {
            this.en_category_id = en_category_id;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public int getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(int question_id) {
            this.question_id = question_id;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }

        public List<AnswersBean> getAnswers() {
            return answers;
        }

        public void setAnswers(List<AnswersBean> answers) {
            this.answers = answers;
        }

        public static class AnswersBean {
            /**
             * answer_id : 296
             * answer : tseitsrite
             * correct_answer : 0
             */

            private int answer_id;
            private String answer;
            private int correct_answer;

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
    }
}
