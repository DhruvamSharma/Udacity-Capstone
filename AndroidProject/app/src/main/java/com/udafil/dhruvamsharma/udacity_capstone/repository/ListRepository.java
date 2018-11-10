package com.udafil.dhruvamsharma.udacity_capstone.repository;


import android.content.Context;

import com.udafil.dhruvamsharma.udacity_capstone.database.DatabaseInstance;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.List;

import java.util.Date;

public class ListRepository {

    private static ListRepository sRepository;
    private static final Object LOCK = new Object();
    private static DatabaseInstance mDb;


    private ListRepository(Context context) {

        mDb = DatabaseInstance.getInstance(context);

    }

    public void insertList(List currentList) {

        mDb.getListDao().insertList(currentList);

    }

    public void updateList(List currentList) {

        mDb.getListDao().updateList(currentList);
    }

    public java.util.List<List> getAllLists(int userId) {
        return mDb.getListDao().getAllLists(userId);
    }

    public List createTempList(int userId) {

        //TODO 2: Change User ID
        List tempList = new List(userId, "My List", new Date());

        mDb.getListDao().insertList(tempList);

        return tempList;

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

    public List getList(int listId) {

        return mDb.getListDao().getCurrentList(listId);

    }
}
