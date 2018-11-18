package com.udafil.dhruvamsharma.udacity_capstone.database.domain;

import com.udafil.dhruvamsharma.udacity_capstone.helper.DateConverter;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity (tableName = "users")
public class User {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo (name = "user_name")
    private String name;

    @ColumnInfo (name = "created_at")
    private Date createdAt;

    @ColumnInfo (name = "user_password")
    private String password;

    @ColumnInfo (name = "user_email")
    private String emailId;

    @ColumnInfo (name = "user_is_signed_in")
    private Boolean isSignedIn;

    @ColumnInfo (name = "user_score")
    private int score;

    public User(int userId, String name, Date createdAt, String password, String emailId, Boolean isSignedIn, int score) {
        this.userId = userId;
        this.name = name;
        this.createdAt = createdAt;
        this.password = password;
        this.emailId = emailId;
        this.isSignedIn = isSignedIn;
        this.score = score;
    }

    @Ignore
    public User(String name, Date createdAt, String password, String emailId, Boolean isSignedIn, int score) {

        this.name = name;
        this.createdAt = createdAt;
        this.password = password;
        this.emailId = emailId;
        this.isSignedIn = isSignedIn;
        this.score = score;
    }

    @Ignore
    public User() {

    }

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
