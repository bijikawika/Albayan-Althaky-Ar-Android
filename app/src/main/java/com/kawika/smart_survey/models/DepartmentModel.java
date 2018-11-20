package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 22/02/18.
 */

public class DepartmentModel {

    /**
     * status : 6000
     * message : Departments lists
     * data : [{"department_id":1,"en_department":"Human Resource Management."},{"department_id":2,"en_department":"Administration"},{"department_id":3,"en_department":"Research and Development "},{"department_id":4,"en_department":"Accounting and Finance."},{"department_id":5,"en_department":"Purchasing"},{"department_id":6,"en_department":"Marketing "}]
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
         * department_id : 1
         * en_department : Human Resource Management.
         */

        private int department_id;
        private String department_name;

        public int getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(int department_id) {
            this.department_id = department_id;
        }

        public String getDepartment_name() {
            return department_name;
        }

        public void setDepartment_name(String en_department) {
            this.department_name = department_name;
        }
    }
}
