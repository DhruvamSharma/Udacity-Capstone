package com.onearticleoneweek.wahadatkashmiri.loginlib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;

import org.parceler.Parcels;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton addUser;
    private TextInputEditText userName, password, passwordAgain;
    private int userId;
    private String response;

    private static SignUpCallbacks signupCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupActivity();


    }

    public static void init(Context context) {
        signupCallbacks = (SignUpCallbacks) context;
    }

    private void setupActivity() {

        userName = findViewById(R
                .id.login_activity_username_edit_task_et);
        password = findViewById(R
                .id.login_activity_password_edit_task_et);
        passwordAgain = findViewById(R
                .id.login_activity_password_again_edit_task_et);

        addUser = findViewById(R.id.sign_up_btn);


        final Intent intent = getIntent();

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser(intent);
            }
        });



    }

    private void saveUser(Intent intent) {

        if(intent.hasExtra(getResources()
                .getString(R.string.current_user))) {

            final User currentUser = Parcels.unwrap(intent
                    .getParcelableExtra(getResources()
                            .getString(R.string.current_user)));

            final String email = userName.getText().toString();
            if(matchPasswords()) {

                LiveData<User> userLiveData = UserRepository.
                        getUserRepository(LoginActivity.this).getUser(currentUser.getUserId());

                userLiveData.observe(LoginActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(final User user) {

                        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {

                                if(checkForUserPresenceAlready(user.getEmailId())) {
                                    addUser(user, email,
                                            password.getText().toString()
                                            , "email",
                                            currentUser.getScore());


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            signupCallbacks.onSignUpComplete();
                                            finish();

                                        }
                                    });
                                } else {
                                    response = "User already present";
                                    signupCallbacks.onSignUpFailed(response);
                                    finish();
                                }

                            }
                        });



                    }
                });

            } else {
                response = "Sorry, passwords do not match";
                signupCallbacks.onSignUpFailed(response);
                finish();
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
    private void addUser(User user, String name, String password, String emailId, int score) {

        user.setName(name);
        user.setEmailId(emailId);
        user.setPassword(password);
        user.setScore(score);
        user.setSignedIn(true);
        //Updating the user for now
        //But actually a new user should be added everytime
        UserRepository.getUserRepository(LoginActivity.this).updateUser(user);

    }

    public interface SignUpCallbacks {

        void onSignUpComplete();
        void onSignUpFailed(String response);

    }





}
