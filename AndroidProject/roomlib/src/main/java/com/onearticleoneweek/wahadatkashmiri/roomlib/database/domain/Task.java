package com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain;

import android.os.Parcelable;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
@Parcel
public class Task implements Parcelable {

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

    protected Task(android.os.Parcel in) {
        taskId = in.readInt();
        taskDescription = in.readString();
        byte tmpIsComlpleted = in.readByte();
        isComlpleted = tmpIsComlpleted == 0 ? null : tmpIsComlpleted == 1;
        listId = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(android.os.Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {

        parcel.writeInt(taskId);
        parcel.writeString(taskDescription);
        parcel.writeInt(listId);
        parcel.writeLong(createdAt.getTime());
        parcel.writeByte((byte) (isComlpleted ? 1 : 0));

    }
}
