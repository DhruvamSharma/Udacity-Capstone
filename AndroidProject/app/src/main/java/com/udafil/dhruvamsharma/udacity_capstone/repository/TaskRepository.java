package com.udafil.dhruvamsharma.udacity_capstone.repository;


import android.content.Context;

import com.udafil.dhruvamsharma.udacity_capstone.database.DatabaseInstance;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;

import java.util.List;

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

    public List<Task> getAllTasks() {

        return mDb.getTaskDao().getAllTasks();

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