package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.onearticleoneweek.wahadatkashmiri.firstentrymodule.FirstTimeInstallActivivity;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;

import java.util.Date;

public class SplashScreen extends AppCompatActivity implements FirstTimeInstallActivivity.StartAppInterface {

    //A common taskRepository for all the network and
    //database operations
    private TaskRepository taskRepository;
    private ListRepository listRepository;
    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash_screen);

        userRepository = UserRepository.getUserRepository(SplashScreen.this);
        listRepository = ListRepository.getCommonRepository(SplashScreen.this);

        //Check for first-time installs
        //Check for Last accessed items
        setUpSharedPreferences();

    }

    /**
     * This method takes the responsibility
     * of setting the shared preferences.
     */
    private void setUpSharedPreferences() {

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

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
        });






    }

    private void firsTimeSetUp(final SharedPreferences preferences) {

        final User tempUser = new User("User", new Date(),
                "password", "emailId", false, 0);

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

    private void setupActivity(final int listId, final int userId, final boolean isFirstTime) {

        AppExecutor.getsInstance().getMainThread().execute(new Runnable() {
            @Override
            public void run() {

                if(isFirstTime) {

                    FirstTimeInstallActivivity.init(SplashScreen.this);

                    //TODO open up NewEntering Screen
                    Intent intent = new Intent(SplashScreen.this, FirstTimeInstallActivivity.class);
                    intent.putExtra(getResources().getString(R.string.current_list), listId);
                    intent.putExtra(getResources().getString(R.string.current_user), userId);
                    intent.putExtra(getResources().getString(R.string.is_first_time_install), isFirstTime);
                    startActivity(intent);

                } else {
                    startApp(listId, userId, isFirstTime);
                }
            }
        });



    }

    private void startApp(int listId, int userId, boolean isFirstTime) {

        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.current_list), listId);
        intent.putExtra(getResources().getString(R.string.current_user), userId);
        intent.putExtra(getResources().getString(R.string.is_first_time_install), isFirstTime);

        startActivity(intent);
        finish();

    }


    @Override
    public void start(int intExtra, int extra, boolean booleanExtra) {

        startApp(intExtra, extra, booleanExtra);
        finish();

    }
}



