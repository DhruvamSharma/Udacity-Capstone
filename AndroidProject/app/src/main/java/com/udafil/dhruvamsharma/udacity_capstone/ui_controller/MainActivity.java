package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onearticleoneweek.wahadatkashmiri.loginlib.LoginActivity;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.MyGoalsWidget;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.BottomSheetListAdapter;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.NewListActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.MainActivityBottomSheetFragment;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.MainActivityTaskListAdapter;


import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This is the activity that displays the
 * tasks and the list to the user
 */
public class MainActivity extends AppCompatActivity
        implements MainActivityBottomSheetFragment.BottomSheetCallBacks,
        BottomSheetListAdapter.ListClickListener,
        LoginActivity.SignUpCallbacks {

    //recycler view for all the tasks
    private RecyclerView mTaskList;
    //recyclerview for all the lists
    private RecyclerView mListList;

    //A common taskRepository for all the network and
    //database operations
    private TaskRepository taskRepository;
    private ListRepository listRepository;
    private UserRepository userRepository;

    //List name
    private TextView mListName;

    //Task Adapter
    MainActivityTaskListAdapter mTaskAdapter;

    //List Adapter
    BottomSheetListAdapter mListAdapter;

    MainActivityBottomSheetFragment mBottomSheetFragment
            = new MainActivityBottomSheetFragment();


    private User currentUser;
    private List currentList;
    private java.util.List<Task> allTasks;
    private java.util.List<List> allLists;


    //BottomSheet for List
    BottomSheetBehavior sheetBehavior;
    ConstraintLayout bottomSheet;


    /**
     * Method when the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpActivity();


    }

    /**
     * This is a setup method
     * This methods is reuired for setting up the activity.
     * It takes care of all the nitty-gritty details
     */
    private void setUpActivity() {

        //Setting up stetho Library
        Stetho.initializeWithDefaults(this);

        //setting up the taskRepository
        listRepository = ListRepository.getCommonRepository(MainActivity.this);
        taskRepository = TaskRepository.getCommonRepository(MainActivity.this);
        userRepository = UserRepository.getUserRepository(MainActivity.this);



        //Check for first-time installs
        //Check for Last accessed items
        setUpSharedPreferences();



        setUpTaskRecyclerView();
        setUpListRecyclerView();
        setUpAds();
        
        

        MaterialButton newListButton
                = findViewById(R.id.main_activity_bottom_sheet_create_list_btn);
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppExecutor
                        .getsInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        if(listChecks()) {
                            final Intent intent
                                    = new Intent(new Intent(
                                            MainActivity.this, NewListActivity.class));
                            intent.putExtra(
                                    getResources().getString(R.string.current_user)
                                    , currentUser.getUserId());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(intent);
                                }
                            });
                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Ask for login
                                    final Intent intent
                                            = new Intent(MainActivity.this,
                                            LoginActivity.class);
                                    Parcelable user = Parcels.wrap(currentUser);
                                    intent.putExtra(getResources().getString(R.string.current_user), user);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(intent);
                                            LoginActivity.init(getContext());
                                        }
                                    });

                                }
                            });


                        }

                    }
                });




            }
        });

        FloatingActionButton newTaskButton = findViewById(R.id.main_activity_new_task_fab);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setupBottomSheet();

            }
        });

        //setUpListBottomSheet();

    }

    private void setUpListBottomSheet() {

        bottomSheet = findViewById(R.id.activity_main_bottom_sheet_bs);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

    }


    /**
     * This method takes the responsibility
     * of setting up the list recycler view.
     */
    private void setUpListRecyclerView() {

        mListList = findViewById(R.id.list_list_main_activity_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mListAdapter = new BottomSheetListAdapter(MainActivity.this);

        mListList.setLayoutManager(layoutManager);
        mListList.setAdapter(mListAdapter);

    }


    /**
     * This method takes the responsibility
     * of setting up the task recycler view.
     */
    private void setUpTaskRecyclerView() {

        mTaskList = findViewById(R.id.task_list_main_activity_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mTaskAdapter = new MainActivityTaskListAdapter(this);

        mTaskList.setLayoutManager(layoutManager);
        mTaskList.setAdapter(mTaskAdapter);

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

        //List Name Text View
        mListName = findViewById(R.id.list_name_main_activity_tv);

        if(isFirstTime) {

            firsTimeSetUp(preferences);

        } else {

            int userId = preferences
                    .getInt("user", -1);


            int listId = preferences.
                    getInt("list", -1);

            setupActivity(listId, userId, false, preferences);
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
                        setupActivity(listId, userId, true, preferences);
                    }
                });


            }
        });





    }



    private void setupActivity(final int listId, int userId,
                               final boolean isFirstTime, final SharedPreferences preferences) {

        LiveData<User> userLiveData = userRepository.getUser(userId);

        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;

                LiveData<List> listLiveData = listRepository.getList(listId);

                listLiveData.observe(MainActivity.this, new Observer<List>() {
                    @Override
                    public void onChanged(List list) {

                        currentList = list;

                        if (isFirstTime) {

                            allTasks = new ArrayList<>();
                            allLists = new ArrayList<>();
                            allLists.add(currentList);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean(getResources()
                                    .getString(R.string.is_first_time_install), false);
                            editor.apply();
                        }

                        retrieveLists(currentUser.getUserId());
                        retrieveTasks(currentList.getListId());

                        if (currentList != null) {

                            mListName.setText(currentList.getListName());
                        }
                        else {

                            //TODO 8: Finish the application gracefully
                            //finish();
                        }


                    }
                });

            }
        });

    }



    /**
     * This methods set ups the ads
     * on the main activity
     */
    private void setUpAds() {

        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));

    }



    /**
     * This method takes care of
     * setting up the bottom sheet
     * and passing the list id
     * as a bundle
     */
    private void setupBottomSheet() {

        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.current_list), String.valueOf(currentList.getListId()));

        mBottomSheetFragment.setArguments(bundle);
        mBottomSheetFragment.show(getSupportFragmentManager(), mBottomSheetFragment.getTag());
    }




    private void retrieveTasks(final int listId) {

        LiveData<java.util.List<Task>> tasksLiveData =
                taskRepository.getAllTasks(listId, false);

        tasksLiveData.observe(this, new Observer<java.util.List<Task>>() {
            @Override
            public void onChanged(java.util.List<Task> tasks) {

                allTasks = tasks;
                if(allTasks != null)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTaskAdapter.updateTasksData(allTasks);
                            mTaskAdapter.updateUser(currentUser);

                            //TODO Add this code somewhere else
                            if(currentUser != null)
                                MyGoalsWidget.setUpData(currentUser.getUserId(), getContext());
                        }
                    });
            }
        });

    }

    private void retrieveLists(final int userId) {

        LiveData<java.util.List<List>> listLiveData =
                listRepository.getAllLists(userId);

        listLiveData.observe(this, new Observer<java.util.List<List>>() {
            @Override
            public void onChanged(java.util.List<List> lists) {
                allLists = lists;

                if(allLists != null)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListAdapter.updateListsData(allLists);
                        }
                    });
            }
        });

    }




    @Override
    protected void onPause() {
        super.onPause();

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getContext().getSharedPreferences("my_file", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("user", currentUser.getUserId());
                editor.putInt("list", currentList.getListId());
                editor.apply();

            }
        });

    }

    @Override
    public void onBottomSheetDismiss() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentUser != null)
        retrieveLists(currentUser.getUserId());
    }

    @Override
    public void onListClick(int listId) {

//        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
//        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        setupActivity(listId, currentUser.getUserId(), false, null);

    }

    /**
     * This methods checks for all the checks to be made
     * before a user can create a list
     * @return boolean
     */
    private boolean listChecks() {

        boolean canMakeList = true;

        if(!currentUser.getSignedIn()) {
            if(!listRepository.canMakeMoreList()) {
                canMakeList = false;
            } else {
                //Do nothing
            }
        } else {
            //Do nothing
        }

        return canMakeList;
    }

    @Override
    public void onSignUpComplete() {

        setupActivity(currentList.getListId(), currentUser.getUserId(),
                false, null);

    }

    @Override
    public void onSignUpFailed(String response) {

        Toast.makeText(MainActivity.this,
                response, Toast.LENGTH_SHORT).show();

    }

    private Context getContext() {
        return MainActivity.this;
    }



}
