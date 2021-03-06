package com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "lists")
@Parcel
public class List {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "list_id")
    private int listId;

    @ColumnInfo (name = "user_id")
    private int userId;

    @ColumnInfo (name = "list_name")
    private String listName;

    @ColumnInfo (name = "created_at")
    private Date createdAt;

    public List(int listId, int userId, String listName, Date createdAt) {
        this.listId = listId;
        this.userId = userId;
        this.listName = listName;
        this.createdAt = createdAt;
    }

    @Ignore
    public List(int userId, String listName, Date createdAt) {
        this.userId = userId;
        this.listName = listName;
        this.createdAt = createdAt;
    }

    @Ignore
    @ParcelConstructor
    public List() {

    }


    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
