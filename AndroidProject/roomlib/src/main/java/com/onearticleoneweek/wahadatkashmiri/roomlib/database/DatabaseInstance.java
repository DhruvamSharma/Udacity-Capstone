package com.onearticleoneweek.wahadatkashmiri.roomlib.database;

import android.content.Context;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.dao.ListDao;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.dao.TaskDao;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.dao.UserDao;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Task.class, List.class, User.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class DatabaseInstance extends RoomDatabase {


    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "goalss";
    private static DatabaseInstance sInstance;

    public static DatabaseInstance getInstance(Context context) {

        if(sInstance == null) {
            synchronized (LOCK) {

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DatabaseInstance.class,
                        DatabaseInstance.DATABASE_NAME)
                        .build();

            }
        }

        return sInstance;
    }

    public abstract TaskDao getTaskDao();
    public abstract ListDao getListDao();
    public abstract UserDao getUserDao();

}
