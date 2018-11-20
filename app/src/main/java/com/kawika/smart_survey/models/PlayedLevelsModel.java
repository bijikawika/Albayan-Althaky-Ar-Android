package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 05/03/18.
 */

public class PlayedLevelsModel {

    /**
     * status : 6000
     * message : Level step lists
     * data : [{"step_id":1,"steps":1,"level_id":2,"en_category_id":2,"step_complete":0},{"step_id":2,"steps":2,"level_id":2,"en_category_id":2,"step_complete":0}]
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
         * step_id : 1
         * steps : 1
         * level_id : 2
         * en_category_id : 2
         * step_complete : 0
         */

        private int step_id;
        private int steps;
        private int level_id;
        private int en_category_id;
        private int step_complete;

        public int getStep_id() {
            return step_id;
        }

        public void setStep_id(int step_id) {
            this.step_id = step_id;
        }

        public int getSteps() {
            return steps;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }

        public int getLevel_id() {
            return level_id;
        }

        public void setLevel_id(int level_id) {
            this.level_id = level_id;
        }

        public int getEn_category_id() {
            return en_category_id;
        }

        public void setEn_category_id(int en_category_id) {
            this.en_category_id = en_category_id;
        }

        public int getStep_complete() {
            return step_complete;
        }

        public void setStep_complete(int step_complete) {
            this.step_complete = step_complete;
        }
    }
}
