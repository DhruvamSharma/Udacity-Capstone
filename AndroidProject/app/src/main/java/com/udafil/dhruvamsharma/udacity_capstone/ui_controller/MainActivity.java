package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.repository.CommonRepository;

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
    //A common repository for all the network and
    //database operations
    private CommonRepository repository;

    //Task Adapter
    MainActivityTaskListAdapter mAdapter;

    MainActivityBottomSheetFragment mBottomSheetFragment;

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

        //setting up the repository
        repository = CommonRepository.getCommonRepository(MainActivity.this);

        mTaskList = findViewById(R.id.task_list_main_activity_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mAdapter = new MainActivityTaskListAdapter(this);

        mTaskList.setLayoutManager(layoutManager);
        mTaskList.setAdapter(mAdapter);

        setUpAds();

        FloatingActionButton newTaskButton = findViewById(R.id.main_activity_new_task_fab);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setupBottomSheet();

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

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.updateTasksData(repository.getAllTasks());


    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("error", "onPause");
    }

    private void setupBottomSheet() {

        mBottomSheetFragment = new MainActivityBottomSheetFragment();
        mBottomSheetFragment.show(getSupportFragmentManager(), mBottomSheetFragment.getTag());
    }

    @Override
    public void onBottomSheetDismiss() {

        mAdapter.updateTasksData(repository.getAllTasks());

    }
}
