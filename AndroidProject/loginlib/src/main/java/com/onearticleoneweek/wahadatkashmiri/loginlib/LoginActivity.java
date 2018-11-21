package com.onearticleoneweek.wahadatkashmiri.loginlib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;

import org.parceler.Parcels;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton addUser;
    private TextInputEditText userName, password, passwordAgain;
    private int userId;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupActivity();


    }

    private void setupActivity() {

        userName = findViewById(R
                .id.login_activity_username_edit_task_et);
        password = findViewById(R
                .id.login_activity_password_edit_task_et);
        passwordAgain = findViewById(R
                .id.login_activity_password_again_edit_task_et);

        Intent intent = getIntent();



        if(intent.hasExtra(getResources()
                .getString(R.string.current_user))) {

            final User currentUser = Parcels.unwrap(intent
                    .getParcelableExtra(getResources()
                            .getString(R.string.current_user)));

            final String email = userName.getText().toString();
            if(matchPasswords()) {
                
                AppExecutor
                        .getsInstance()
                        .getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(!checkForUserPresenceAlready(email))
                        {

                            addUser(email,
                                    password.getText().toString()
                                    , "email",
                                    currentUser.getScore());

                            //finish();
                            
                        } else {
                            response = "User already present";
                        }
                    }
                });
                
                
            } else {
                response = "Sorry, passwords do not match";
            }

        } else {
            //TODO end activity gracefully
        }

    }

    /**
     *
     * @return
     */
    private boolean matchPasswords() {

        boolean canSaveUser = false;

        if(passwordAgain.getText().toString().equals(password.getText().toString())) {
            canSaveUser = true;
        }

        return canSaveUser;
    }

    /**
     *
     * @param email
     * @return
     */
    private boolean checkForUserPresenceAlready(String email) {

        Boolean userPresence = false;

        int userCount = UserRepository
                .getUserRepository(LoginActivity.this).findUserByEmail(email);
        if(userCount != 0 ) {
            userPresence = true;
        } else {
            //Do nothing
        }

        return userPresence;

    }

    /**
     *
     * @param name
     * @param password
     * @param score
     */
    private void addUser(String name, String password, String emailId, int score) {

        User user = new User(name, new Date(), password, emailId, true, score);
        //Updating the user for now
        //But actually a new user should be added everytime
        UserRepository.getUserRepository(LoginActivity.this).updateUser(user);

    }



}
