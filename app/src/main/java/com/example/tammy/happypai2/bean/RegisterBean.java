package com.example.tammy.happypai2.bean;

/**
 * Created by tammy on 17/6/25.
 */

public class RegisterBean {

    /**
     * state : 0
     * msg : Success
     * user_id : 6
     */

    private int state;
    private String msg;
    private int user_id;

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
