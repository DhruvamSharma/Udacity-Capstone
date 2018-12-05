package com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository;


import android.content.Context;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.DatabaseInstance;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * This is a repository for all the network and database calls
 * It follows a singleton pattern
 */
public class TaskRepository {

    private static TaskRepository sRepository;
    private static final Object LOCK = new Object();
    private static DatabaseInstance mDb;

    private TaskRepository(Context context) {

        mDb = DatabaseInstance.getInstance(context);

    }

    public void insertTask(Task currentTask) {


        mDb.getTaskDao().insertTask(currentTask);

    }

    public void updateTask(Task currentTask) {

        mDb.getTaskDao().updateTask(currentTask);

    }

    public LiveData<List<Task>> getAllTasks(int listId, Boolean isCompleted) {

        return mDb.getTaskDao().getAllTasks(listId, isCompleted);

    }

    public void deleteTask(Task task) {

        mDb.getTaskDao().deleteTask(task);

    }

    public List<Task> getAllTasksWithoutLiveData(int listId, Boolean isCompleted) {
        return mDb.getTaskDao().getAllTasksWithoutLiveData(listId, isCompleted);
    }


    /**
     * This method makes sure that Repository
     * follows singleton pattern
     * @param context
     * @return
     */
    public static TaskRepository getCommonRepository(Context context) {

        if(sRepository == null) {
            synchronized (LOCK) {

                sRepository = new TaskRepository(context);

            }


        }

        return sRepository;

    }


}
