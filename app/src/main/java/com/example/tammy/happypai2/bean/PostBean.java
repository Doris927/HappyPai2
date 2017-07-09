package com.example.tammy.happypai2.bean;

import java.util.List;

/**
 * Created by admin on 7/6/17.
 */

public class PostBean {

    /**
     * state : 0
     * msg : Success
     * shares : [{"user_name":"qwe","user_id":"9","state_id":"36","state_text":"cboquwfbo32","location":null,"time_stamp":"2017-07-06 10:31:21","followee_id":null},{"user_name":"qwe","user_id":"9","state_id":"35","state_text":"cboquwfbo32","location":null,"time_stamp":"2017-07-06 10:31:12","followee_id":null},{"user_name":"tammy","user_id":"3","state_id":"5","state_text":"qee ","location":" 滋賀県大津市松が丘６丁目１−１","time_stamp":"2017-06-26 10:26:54","followee_id":"3"}]
     */

    private int state;
    private String msg;
    private List<SharesBean> shares;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SharesBean> getShares() {
        return shares;
    }

    public void setShares(List<SharesBean> shares) {
        this.shares = shares;
    }

    public static class SharesBean {
        /**
         * user_name : qwe
         * user_id : 9
         * state_id : 36
         * state_text : cboquwfbo32
         * location : null
         * time_stamp : 2017-07-06 10:31:21
         * followee_id : null
         */

        private String user_name;
        private String user_id;
        private String state_id;
        private String state_text;
        private String content;
        private Object location;
        private String time_stamp;
        private Object followee_id;
        private String picture;
        private String comment_num;

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getState_id() {
            return state_id;
        }

        public void setState_id(String state_id) {
            this.state_id = state_id;
        }

        public String getState_text() {
            return state_text;
        }

        public void setState_text(String state_text) {
            this.state_text = state_text;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
        }

        public String getTime_stamp() {
            return time_stamp;
        }

        public void setTime_stamp(String time_stamp) {
            this.time_stamp = time_stamp;
        }

        public Object getFollowee_id() {
            return followee_id;
        }

        public void setFollowee_id(Object followee_id) {
            this.followee_id = followee_id;
        }

        public String getPicture(){ return picture; }

        public void setPicture(String picture) { this.picture = picture; }
    }
}
