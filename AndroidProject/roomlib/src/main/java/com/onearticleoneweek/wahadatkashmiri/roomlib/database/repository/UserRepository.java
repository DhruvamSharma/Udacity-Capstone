package com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository;


import android.content.Context;


import com.onearticleoneweek.wahadatkashmiri.roomlib.database.DatabaseInstance;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * This is a repository for all the network and database calls
 * It follows a singleton pattern
 */
public class UserRepository {

    private static UserRepository sRepository;
    private static final Object LOCK = new Object();
    private static DatabaseInstance mDb;

    private UserRepository(Context context) {

        mDb = DatabaseInstance.getInstance(context);

    }

    public LiveData<List<User>> getAllUsers() {

        return mDb.getUserDao().getAllUsers();

    }

    public void deleteUser(User currentUser) {

        mDb.getUserDao().deleteUser(currentUser);
    }


    public void updateUser(User currentUser) {

        mDb.getUserDao().updateUser(currentUser);

    }

    public int createUser (User currentUser) {

        return (int) mDb.getUserDao().insertUser(currentUser);

    }

    public int findUserByEmail(String email) {

        return (int) mDb.getUserDao().findUserByEmail(email);
    }


    /**
     * This method makes sure that Repository
     * follows singleton pattern
     * @param context
     * @return
     */
    public static UserRepository getUserRepository(Context context) {

        if(sRepository == null) {

            synchronized (LOCK) {

                sRepository = new UserRepository(context);

            }

        }

        return sRepository;

    }


    public LiveData<User> getUser(int userId) {

        return mDb.getUserDao().getUser(userId);

    }


    public User getUserWithoutLiveData(int userId) {

        return mDb.getUserDao().getUserWithoutLiveData(userId);
    }
}
