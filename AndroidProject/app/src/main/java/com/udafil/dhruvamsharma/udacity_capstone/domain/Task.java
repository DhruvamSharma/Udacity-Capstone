package com.udafil.dhruvamsharma.udacity_capstone.domain;

import java.util.Date;

public class Task {

    private int taskId;

    private String taskDescription;

    private Boolean isComlpleted;

    private int listId;

    private Date createdAt;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Boolean getComlpleted() {
        return isComlpleted;
    }

    public void setComlpleted(Boolean comlpleted) {
        isComlpleted = comlpleted;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
