package com.kawika.smart_survey.models;

import java.util.List;

/**
 * Created by senthiljs on 23/02/18.
 */

public class FollowedTopicsRawModel {

    /**
     * user_id : 21
     * followed_topics : [{"category_id":"3"},{"category_id":"4"}]
     */

    private String user_id;
    private List<FollowedTopicsBean> followed_topics;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<FollowedTopicsBean> getFollowed_topics() {
        return followed_topics;
    }

    public void setFollowed_topics(List<FollowedTopicsBean> followed_topics) {
        this.followed_topics = followed_topics;
    }

    public static class FollowedTopicsBean {
        /**
         * category_id : 3
         */
        private int category_id;

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }
    }
}
