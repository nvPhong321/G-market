package com.example.phong.g_market.model;

/**
 * Created by phong on 2/21/2018.
 */

public class User {
    private String userid;
    private String username;
    private String email;
    private String password;

    public User(){

    }

    public User(String userid, String username, String email) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
