package com.udafil.dhruvamsharma.udacity_capstone.database.dao;

import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks WHERE list_id = :listId")
    List<Task> getAllTasks(int listId);

    @Delete
    void deleteTask(Task currentTask);

    @Insert
    long insertTask(Task currentTask);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task currentTask);

    @Delete
    void deleteAllTasks(Task currentTask);

}
