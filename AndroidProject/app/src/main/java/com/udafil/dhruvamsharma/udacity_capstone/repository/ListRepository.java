package com.udafil.dhruvamsharma.udacity_capstone.repository;


import android.content.Context;

import com.udafil.dhruvamsharma.udacity_capstone.database.DatabaseInstance;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.List;

import java.util.Date;

import androidx.lifecycle.LiveData;

public class ListRepository {

    private static ListRepository sRepository;
    private static final Object LOCK = new Object();
    private static DatabaseInstance mDb;


    private ListRepository(Context context) {

        mDb = DatabaseInstance.getInstance(context);

    }

    public int insertList(List currentList) {

        return (int) mDb.getListDao().insertList(currentList);

    }

    public void updateList(List currentList) {

        mDb.getListDao().updateList(currentList);
    }

    public LiveData<java.util.List<List>> getAllLists(int userId) {
        return mDb.getListDao().getAllLists(userId);
    }


    /**
     * This method makes sure that Repository
     * follows singleton pattern
     * @param context
     * @return
     */
    public static ListRepository getCommonRepository(Context context) {

        if(sRepository == null) {
            synchronized (LOCK) {

                sRepository = new ListRepository(context);

            }


        }

        return sRepository;

    }

    public LiveData<List> getList(int listId) {

        return mDb.getListDao().getCurrentList(listId);

    }
}
