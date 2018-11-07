package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.udafil.dhruvamsharma.udacity_capstone.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This is the activity that displays the
 * tasks and the list to the user
 */
public class MainActivity extends AppCompatActivity {

    //recycler view for all the tasks
    private RecyclerView mTaskList;

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

        mTaskList = findViewById(R.id.task_list_main_activity_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        MainActivityTaskListAdapter adapter = new MainActivityTaskListAdapter(getData());

        mTaskList.setLayoutManager(layoutManager);
        mTaskList.setAdapter(adapter);

        setUpAds();
    }

    /**
     * This methods set ups the ads
     * on the main activity
     */
    private void setUpAds() {

        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));

    }


    private List<String> getData() {

        List<String> data = new ArrayList<>();

        data.add("hello");
        data.add("bye");
        data.add("tata");

        return data;

    }
}
