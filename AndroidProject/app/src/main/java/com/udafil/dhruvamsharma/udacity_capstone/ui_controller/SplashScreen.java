package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;

import java.util.Date;

public class SplashScreen extends AppCompatActivity {

    //A common taskRepository for all the network and
    //database operations
    private TaskRepository taskRepository;
    private ListRepository listRepository;
    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        userRepository = UserRepository.getUserRepository(this);
        listRepository = ListRepository.getCommonRepository(this);

        //Check for first-time installs
        //Check for Last accessed items
        setUpSharedPreferences();

    }

    /**
     * This method takes the responsibility
     * of setting the shared preferences.
     */
    private void setUpSharedPreferences() {

        final SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("my_file", MODE_PRIVATE);

        boolean isFirstTime = preferences.getBoolean(getResources()
                .getString(R.string.is_first_time_install), true);



        if(isFirstTime) {

            firsTimeSetUp(preferences);

        } else {

            int userId = preferences
                    .getInt("user", -1);


            int listId = preferences.
                    getInt("list", -1);

            setupActivity(listId, userId, false);
        }




    }

    private void firsTimeSetUp(final SharedPreferences preferences) {

        final User tempUser = new User("User", new Date(),
                "password", "emailId", false, 0);

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                final int userId = userRepository.createUser(tempUser);
                List tempList = new List(userId, "My List", new Date());
                final int listId = listRepository.insertList(tempList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupActivity(listId, userId, true);
                    }
                });


            }
        });





    }

    private void setupActivity(int listId, int userId, boolean isFirstTime) {

        if(isFirstTime) {

            Toast.makeText(SplashScreen.this, "is first time: "+ isFirstTime, Toast.LENGTH_SHORT).show();


            //TODO open up NewEntering Screen
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.putExtra(getResources().getString(R.string.current_list), listId);
            intent.putExtra(getResources().getString(R.string.current_user), userId);
            intent.putExtra(getResources().getString(R.string.is_first_time_install), isFirstTime);

            startActivity(intent);
        } else {
            Toast.makeText(SplashScreen.this, "is first time: "+ isFirstTime, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.putExtra(getResources().getString(R.string.current_list), listId);
            intent.putExtra(getResources().getString(R.string.current_user), userId);
            intent.putExtra(getResources().getString(R.string.is_first_time_install), isFirstTime);

            startActivity(intent);
        }

    }

}



