package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.List;
import com.udafil.dhruvamsharma.udacity_capstone.repository.ListRepository;
import com.udafil.dhruvamsharma.udacity_capstone.repository.TaskRepository;
import com.udafil.dhruvamsharma.udacity_capstone.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.BottomSheetListAdapter;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.NewListActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.MainActivityBottomSheetFragment;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.MainActivityTaskListAdapter;

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

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        boolean isFirstTime = preferences.getBoolean("is_first_time", true);

        //setting up the taskRepository
        listRepository = ListRepository.getCommonRepository(MainActivity.this);
        taskRepository = TaskRepository.getCommonRepository(MainActivity.this);
        userRepository = UserRepository.getUserRepository(MainActivity.this);

        if(isFirstTime) {




            List currentList = userRepository.createTempUser(MainActivity.this);

            /*TODO 6: This should present the last list user was accessing
                which was stored in onPause()
             */
            mListName = findViewById(R.id.list_name_main_activity_tv);
            mListName.setText(currentList.getListName());



            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean("is_first_time", false);

            editor.apply();

        }

    }

    /**
     * This methods set ups the ads
     * on the main activity
     */
    private void setUpAds() {

        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));

    }

    @Override
    protected void onResume() {
        super.onResume();

        mTaskAdapter.updateTasksData(taskRepository.getAllTasks());
        mListAdapter.updateListsData(listRepository.getAllLists());

    }

    /**
     * This method takes care of
     * setting up the bottom sheet
     */
    private void setupBottomSheet() {

        mBottomSheetFragment.show(getSupportFragmentManager(), mBottomSheetFragment.getTag());
    }

    @Override
    public void onBottomSheetDismiss() {

        mTaskAdapter.updateTasksData(taskRepository.getAllTasks());

    }
}
