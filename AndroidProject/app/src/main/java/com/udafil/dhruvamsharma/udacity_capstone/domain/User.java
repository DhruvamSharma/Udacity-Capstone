package com.udafil.dhruvamsharma.udacity_capstone.domain;

import java.util.Date;

import androidx.room.Entity;

public class User {

    private int userId;

    private String name;

    private Date createdAt;

    private String password;

    private String emailId;

    private Boolean isSignedIn;

    private int score;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Boolean getSignedIn() {
        return isSignedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        isSignedIn = signedIn;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
