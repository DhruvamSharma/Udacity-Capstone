package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.udacity_capstone.pointslib.PointsActivity;
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
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
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
    private RecyclerView mCompletedTaskList;

    //A common taskRepository for all the network and
    //database operations
    private TaskRepository taskRepository;
    private ListRepository listRepository;
    private UserRepository userRepository;

    //List name
    private TextView mCompletedTextLabel;

    //Task Adapter
    MainActivityTaskListAdapter mTaskAdapter;
    MainActivityTaskListAdapter mCompletedTaskAdapter;

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
    Toolbar myToolbar;

    Bundle saveInstanceState;


    /**
     * Method when the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveInstanceState = savedInstanceState;
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        setUpActivity();


    }

    /**
     * This is a setup method
     * This methods is reuired for setting up the activity.
     * It takes care of all the nity-gritty details
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
        setupCompletedTaskRecyclerView();
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
        bottomSheet = findViewById(R.id.activity_main_bottom_sheet_bs);

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

        mTaskAdapter = new MainActivityTaskListAdapter(this, bottomSheet);


        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(MainActivity.this, layoutManager.getOrientation());

        mTaskList.setLayoutManager(layoutManager);
        mTaskList.setAdapter(mTaskAdapter);
        mTaskList.addItemDecoration(dividerItemDecoration);

    }

    private void setupCompletedTaskRecyclerView() {

        mCompletedTaskList = findViewById(R.id.task_list__completed_main_activity_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mCompletedTaskAdapter = new MainActivityTaskListAdapter(this, bottomSheet );


        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(MainActivity.this, layoutManager.getOrientation());

        mCompletedTaskList.setLayoutManager(layoutManager);
        mCompletedTaskList.setAdapter(mCompletedTaskAdapter);
        mCompletedTaskList.addItemDecoration(dividerItemDecoration);

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
        mCompletedTextLabel = findViewById(R.id.completed_task_text_view_tv);

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


    /**
     * This method set ups the live data for the user
     * Which in turn set ups live data for the list
     * @param listId
     * @param userId
     * @param isFirstTime
     * @param preferences
     */
    private void setupActivity(final int listId, int userId,
                               final boolean isFirstTime,
                               final SharedPreferences preferences) {

        setupSingleUserLiveData(listId, userId, isFirstTime, preferences);

    }


    /**
     * This method set ups live data for the user
     * @param listId
     * @param userId
     * @param isFirstTime
     * @param preferences
     */
    private void setupSingleUserLiveData(final int listId, final int userId,
                                         final  boolean isFirstTime,
                                         final SharedPreferences preferences) {

        final LiveData<User> userLiveData = userRepository.getUser(userId);

        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                userLiveData.removeObservers(MainActivity.this);
                setupSingleListLiveData(user, listId, isFirstTime, preferences);

            }
        });

    }

    /**
     * This method set ups live data for the list
     * @param listId
     * @param isFirstTime
     * @param preferences
     */
    private void setupSingleListLiveData(final User user, int listId,
                                         final boolean isFirstTime,
                                         final SharedPreferences preferences) {

        final LiveData<List> listLiveData = listRepository.getList(listId);

        listLiveData.observe(MainActivity.this, new Observer<List>() {
            @Override
            public void onChanged(List list) {

                listLiveData.removeObservers(MainActivity.this);
                setupCurrentListAndUser( user, list, isFirstTime, preferences);


            }
        });

    }

    /**
     * This method is delegated the work for
     * setting up when the data is ready.
     * @param user
     * @param list
     * @param isFirstTime
     * @param preferences
     */
    private void setupCurrentListAndUser(User user, List list,
                                         boolean isFirstTime,
                                         SharedPreferences preferences) {

        currentUser = user;
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
        retrieveTasks(currentList.getListId(), true);
        retrieveTasks(currentList.getListId(), false);

        if (currentList != null) {
            myToolbar.setTitle(currentList.getListName());
            myToolbar.setTitleTextAppearance(MainActivity.this,
                    R.style.TextAppearance_AppCompat_Display2);
            myToolbar.setTitleTextColor(getResources()
                    .getColor(android.R.color.black));

        }
        else {

            //TODO 8: Finish the application gracefully
            //finish();
        }

    }


    /**
     *
     */
    private void retrieveTasks() {

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {

            @Override
            public void run() {
                final java.util.List<Task> incompleteTasks =  taskRepository.
                        getAllTasksWithoutLiveData(currentList.getListId(), false);

                final java.util.List<Task>completedTasks = taskRepository.
                        getAllTasksWithoutLiveData(currentList.getListId(), true);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        onTaskRetrieved(true, completedTasks) ;
                        onTaskRetrieved(false, incompleteTasks);

                        if (currentList != null) {
                            myToolbar.setTitle(currentList.getListName());
                            myToolbar.setTitleTextAppearance(MainActivity.this,
                                    R.style.TextAppearance_AppCompat_Display2);
                            myToolbar.setTitleTextColor(getResources()
                                    .getColor(android.R.color.black));

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




    private void retrieveTasks(final int listId, final boolean isCompleted) {

        Log.e("onTaskRetrieved", "retrieving from database");

        final LiveData<java.util.List<Task>> tasksLiveData =
                taskRepository.getAllTasks(listId, isCompleted);

        tasksLiveData.observe(this, new Observer<java.util.List<Task>>() {
            @Override
            public void onChanged(final java.util.List<Task> tasks) {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("onTaskRetrieved", "retrieving from live data " + isCompleted);

                            onTaskRetrieved(isCompleted, tasks);

                        }
                    });
            }
        });

    }

    private void onCompletedTaskPresent(Boolean isCompletedTaskPresent) {

        if(isCompletedTaskPresent)
        mCompletedTextLabel.setVisibility(View.VISIBLE);
        else
        mCompletedTextLabel.setVisibility(View.GONE);
    }


    /**
     *
     * @param isCompleted
     * @param tasks
     */
    private void onTaskRetrieved(Boolean isCompleted, java.util.List<Task> tasks) {

        allTasks = tasks;

        if(!isCompleted ){

            if(allTasks != null) {
                mTaskList.setVisibility(View.VISIBLE);
                mTaskAdapter.updateTasksData(allTasks);
                mTaskAdapter.updateUser(currentUser);

            } else {

                mTaskList.setVisibility(View.GONE);
                //onCompletedTaskPresent();
            }

        }
        else {

            if(allTasks == null ) {
                onCompletedTaskPresent(false);
            }

            else {

                if(allTasks.size() == 0) {
                    onCompletedTaskPresent(false);
                } else {
                    onCompletedTaskPresent(true);
                }

                mCompletedTaskAdapter.updateTasksData(allTasks);
                mCompletedTaskAdapter.updateUser(currentUser);

            }
        }

        //TODO Add this code somewhere else
        if(currentUser != null)
            MyGoalsWidget.setUpData(currentUser.getUserId(), getContext());

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
    public void onListClick(final List list) {

        setupCurrentListAndUser(currentUser, list, false, null);
//        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
//        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


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
    public void onSignUpComplete(User user) {

        setupCurrentListAndUser(user, currentList,
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_my_points: {

                Intent intent = new Intent(MainActivity.this, PointsActivity.class);

                Parcelable parcel = Parcels.wrap(currentUser);
                intent.putExtra(getResources().getString(R.string.current_user), parcel);
                startActivity(intent);
                break;
            }

        }
        return true;
    }
}
