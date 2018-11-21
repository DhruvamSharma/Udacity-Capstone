package com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
@Parcel
public class Task {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "task_id")
    private int taskId;

    @ColumnInfo (name = "task_description")
    private String taskDescription;

    @ColumnInfo (name = "is_completed")
    private Boolean isComlpleted;

    @ColumnInfo (name = "list_id")
    private int listId;

    @ColumnInfo (name = "created_at")
    private Date createdAt;



    public Task(int taskId, String taskDescription, Boolean isComlpleted, int listId, Date createdAt) {
        this.taskId = taskId;
        this.taskDescription = taskDescription;
        this.isComlpleted = isComlpleted;
        this.listId = listId;
        this.createdAt = createdAt;
    }

    @Ignore
    public Task(String taskDescription, Boolean isComlpleted, int listId, Date createdAt) {

        this.taskDescription = taskDescription;
        this.isComlpleted = isComlpleted;
        this.listId = listId;
        this.createdAt = createdAt;
    }

    @Ignore
    @ParcelConstructor
    public Task() {

    }

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
