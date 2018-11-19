package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.database.DatabaseInstance;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.List;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.User;
import com.udafil.dhruvamsharma.udacity_capstone.helper.AppExecutor;
import com.udafil.dhruvamsharma.udacity_capstone.repository.ListRepository;
import com.udafil.dhruvamsharma.udacity_capstone.repository.TaskRepository;
import com.udafil.dhruvamsharma.udacity_capstone.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.BottomSheetListAdapter;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.NewListActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.MainActivityBottomSheetFragment;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.MainActivityTaskListAdapter;


import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This is the activity that displays the
 * tasks and the list to the user
 */
public class MainActivity extends AppCompatActivity implements MainActivityBottomSheetFragment.BottomSheetCallBacks {

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

        MaterialButton newListButton = findViewById(R.id.main_activity_bottom_sheet_create_list_btn);
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewListActivity.class));
            }
        });

        FloatingActionButton newTaskButton = findViewById(R.id.main_activity_new_task_fab);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setupBottomSheet();

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

        final SharedPreferences preferences = getApplicationContext().getSharedPreferences("my_file", MODE_PRIVATE);

        boolean isFirstTime = preferences.getBoolean(getResources()
                .getString(R.string.is_first_time_install), true);




        //List Name Text View
        mListName = findViewById(R.id.list_name_main_activity_tv);

        if(isFirstTime) {


            AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {

                    // TODO application not working from here
                    currentUser = userRepository.createTempUser();
                    currentList = listRepository.createTempList(currentUser.getUserId());
                    allTasks = new ArrayList<>();
                    allLists = new ArrayList<>();
                    allLists.add(currentList);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(getResources()
                            .getString(R.string.is_first_time_install), false);
                    editor.apply();

                    setupActivity(currentList.getListId(), currentUser.getUserId());

                }
            });

        } else {


            AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {

                    int userId = preferences
                            .getInt("user", -1);


                    int listId = preferences.
                            getInt("list", -1);

                    setupActivity(listId, userId);

                }
            });

        }

    }

    private void setupActivity(int listId, int userId) {

        currentList = listRepository.getList(listId);
        currentUser = userRepository.getUser(userId);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
     */
    private void setupBottomSheet() {

        mBottomSheetFragment.show(getSupportFragmentManager(), mBottomSheetFragment.getTag());
        //Toast was here
    }

    @Override
    public void onBottomSheetDismiss() {

        retrieveTasks(currentList.getListId());

    }


    private void retrieveTasks(final int listId) {

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                allTasks = taskRepository.getAllTasks(listId);
                if(allTasks != null)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTaskAdapter.updateTasksData(allTasks);
                    }
                });

            }
        });

    }

    private void retrieveLists(final int userId) {

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                allLists = listRepository.getAllLists(userId);

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
    protected void onResume() {
        super.onResume();

        if(currentList != null)
        retrieveTasks(currentList.getListId());

    }


    @Override
    protected void onPause() {
        super.onPause();

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("my_file", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("user", currentUser.getUserId());
                editor.putInt("list", currentList.getListId());
                editor.apply();

            }
        });

    }

}
