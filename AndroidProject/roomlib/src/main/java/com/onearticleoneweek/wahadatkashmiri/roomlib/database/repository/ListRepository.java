package com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository;


import android.content.Context;


import com.onearticleoneweek.wahadatkashmiri.roomlib.database.DatabaseInstance;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;

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

    public int getListCount() {

        int listCount = (int) mDb.getListDao().getListCount();
        return listCount;
    }

    public boolean canMakeMoreList() {

        Boolean canMakeList = false;

        if(getListCount() < 3) {
            canMakeList = true;
        } else {
            // Do nothing
        }

        return canMakeList;

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

    public java.util.List<List> getListsWithoutLiveData(int userId) {
        return mDb.getListDao().getListWithoutLiveData(userId);
    }

    public List getSingleListWithoutLiveData(int listId) {
        return  mDb.getListDao().getSingleListWithoutLiveData(listId);
    }
}
