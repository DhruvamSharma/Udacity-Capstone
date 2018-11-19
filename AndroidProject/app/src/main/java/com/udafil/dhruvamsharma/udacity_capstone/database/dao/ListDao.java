package com.udafil.dhruvamsharma.udacity_capstone.database.dao;

import com.udafil.dhruvamsharma.
        udacity_capstone.database.domain.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ListDao {

    @Query("SELECT * FROM lists WHERE user_id = :userId")
    LiveData<java.util.List<List>> getAllLists(int userId);

    @Delete
    void deleteList(List currentList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateList(List currentList);

    @Insert
    long insertList(List currentList);

    @Delete
    void deleteAllLists(List currentList);

    @Query("SELECT * FROM lists WHERE list_id = :listId")
    LiveData<List> getCurrentList(int listId);

    @Query("SELECT * FROM lists WHERE list_id = :listId")
    List getTempList(int listId);
}
