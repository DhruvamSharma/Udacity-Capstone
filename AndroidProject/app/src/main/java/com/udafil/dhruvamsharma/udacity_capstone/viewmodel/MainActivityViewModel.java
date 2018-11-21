package com.udafil.dhruvamsharma.udacity_capstone.viewmodel;

import android.app.Application;

import com.udafil.dhruvamsharma.udacity_capstone.database.DatabaseInstance;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.List;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<User> currentUser;
    private DatabaseInstance mDb;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        mDb = DatabaseInstance.getInstance(application.getApplicationContext());
    }

    public LiveData<User> getCurrentUser(int id) {

        return mDb.getUserDao().getUser(id);

    }

}
