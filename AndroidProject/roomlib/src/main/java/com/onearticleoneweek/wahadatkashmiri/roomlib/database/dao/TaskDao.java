package com.onearticleoneweek.wahadatkashmiri.roomlib.database.dao;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks WHERE list_id = :listId AND is_completed = :isCompleted")
    LiveData<List<Task>> getAllTasks(int listId, boolean isCompleted);

    @Delete
    void deleteTask(Task currentTask);

    @Insert
    long insertTask(Task currentTask);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task currentTask);

    @Delete
    void deleteAllTasks(Task currentTask);

    @Query("SELECT * FROM tasks WHERE list_id = :listId AND is_completed = :isCompleted")
    List<Task> getAllTasksWithoutLiveData(int listId, Boolean isCompleted);
}
