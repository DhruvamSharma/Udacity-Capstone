package com.udafil.dhruvamsharma.udacity_capstone.repository;


import android.content.Context;

import com.udafil.dhruvamsharma.udacity_capstone.database.DatabaseInstance;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.User;

import java.util.Date;
import java.util.List;

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

    public List<User> getAllUsers() {

        return mDb.getUserDao().getAllUsers();

    }

    public void deleteUser(User currentUser) {

        mDb.getUserDao().deleteUser(currentUser);
    }


    public void updateUser(User currentUser) {

        mDb.getUserDao().updateUser(currentUser);

    }

    public void createUser (User currentUser) {

        mDb.getUserDao().insertUser(currentUser);

    }


    public com.udafil.dhruvamsharma.
            udacity_capstone.database.domain.List createTempUser(Context context) {

        User tempUser = new User("User", new Date(),
                "password", "emailId", false, 0);

        mDb.getUserDao().insertUser(tempUser);

        //TODO 2: Change User ID
        com.udafil.dhruvamsharma.udacity_capstone.
                database.domain.List currentList = new com.udafil.dhruvamsharma.
                udacity_capstone.database.domain.List(tempUser.getUserId(), "My List", new Date());

        ListRepository.getCommonRepository(context).insertList(currentList);

        return ListRepository.getCommonRepository(context).getAllLists().get(0);

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




}
