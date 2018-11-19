package com.udafil.dhruvamsharma.udacity_capstone.database.dao;

import com.udafil.dhruvamsharma.udacity_capstone.database.domain.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Delete
    void deleteUser(User currentUser);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User currentUser);

    @Insert
    long insertUser(User currentUser);

    @Query("SELECT * FROM users WHERE user_id = :userId")
    LiveData<User> getUser(int userId);

    @Query("SELECT * FROM users WHERE user_id = :userId")
    User getTempUser(int userId);
}
