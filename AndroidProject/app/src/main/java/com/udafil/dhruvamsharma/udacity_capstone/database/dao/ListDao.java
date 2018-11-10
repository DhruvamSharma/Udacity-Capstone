package com.udafil.dhruvamsharma.udacity_capstone.database.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ListDao {

    @Query("SELECT * FROM list WHERE user_id = :userId")
    List<com.udafil.dhruvamsharma.
            udacity_capstone.database.domain.List> getAllLists(int userId);

    @Delete
    void deleteList(com.udafil.dhruvamsharma.
                            udacity_capstone.database.domain.List currentList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateList(com.udafil.dhruvamsharma.
                            udacity_capstone.database.domain.List currentList);

    @Insert
    void insertList(com.udafil.dhruvamsharma.
                            udacity_capstone.database.domain.List currentList);

    @Delete
    void deleteAllLists(com.udafil.dhruvamsharma.
                                udacity_capstone.database.domain.List currentList);

    @Query("SELECT * FROM list WHERE list_id = :listId")
    com.udafil.dhruvamsharma.
            udacity_capstone.database.domain.List getCurrentList(int listId);
}
