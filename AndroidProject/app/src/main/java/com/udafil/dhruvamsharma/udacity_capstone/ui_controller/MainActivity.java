package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.onearticleoneweek.wahadatkashmiri.loginlib.LoginActivity;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;
import com.udacity_capstone.pointslib.PointsActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.UpdateListActivity;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.BottomSheetListAdapter;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.NewListActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.CompletedTaskActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.MainActivityBottomSheetFragment;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task.MainActivityTaskListAdapter;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.widget.MyGoalsWidget;


import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
        LoginActivity.SignUpCallbacks,
        UpdateListActivity.UpdateListCallBacks,
        NewListActivity.NewListCallBacks,
        MainActivityTaskListAdapter.UpdateCallbacks {

    //recycler view for all the tasks
    private RecyclerView mTaskList;
    //recyclerview for all the lists
    private RecyclerView mListList;


    //A common taskRepository for all the network and
    //database operations
    private TaskRepository taskRepository;
    private ListRepository listRepository;
    private UserRepository userRepository;



    //Task Adapter
    MainActivityTaskListAdapter mTaskAdapter;

    //List Adapter
    BottomSheetListAdapter mListAdapter;

    MainActivityBottomSheetFragment mBottomSheetFragment;


    private User currentUser;
    private List currentList;
    private java.util.List<Task> allTasks;
    private java.util.List<List> allLists;


    //BottomSheet for List
    BottomSheetBehavior sheetBehavior;
    ConstraintLayout bottomSheet;
    CoordinatorLayout mParentLayout;
    Toolbar myToolbar, bottomSheetToolbar;

    Bundle saveInstanceState;

    private ConstraintLayout mNoTasksPresentLayout;


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




        setUpListBottomSheet();

        bottomSheetToolbar = bottomSheet.findViewById(R.id.my_toolbar_bottom_sheet);
        bottomSheetToolbar.setLogo(getResources().getDrawable(R.drawable.round_reorder_24));

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


        if(getIntent().hasExtra(getResources().getString(R.string.is_first_time_install)) &&
                getIntent().hasExtra(getResources().getString(R.string.current_user))&&
                getIntent().hasExtra(getResources().getString(R.string.current_list))) {

            Intent intent = getIntent();


            setupActivity(intent.getIntExtra(getResources().getString(R.string.current_list), -1),
                    intent.getIntExtra(getResources().getString(R.string.current_user), -1),
                    intent.getBooleanExtra(getResources().getString(R.string.is_first_time_install), true));

        } else {

            //TODO finish app gracefully
            finish();

        }






        setUpTaskRecyclerView();
        setUpListRecyclerView();

        setUpAds();
        
        

        MaterialButton newListButton
                = findViewById(R.id.main_activity_bottom_sheet_create_list_btn);
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        checkIfUserCanCreateList();

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

        mNoTasksPresentLayout = findViewById(R.id.no_tasks_present_layout);


    }

    private void checkIfUserCanCreateList() {

        if(listChecks()) {
            NewListActivity.init(getContext());
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

    private void setUpListBottomSheet() {

        mParentLayout = findViewById(R.id.main_activity_parent_layout);

        bottomSheet = mParentLayout.findViewById(R.id.bottom_sheet_fragment_layout);
        if(bottomSheet != null) {
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
        runLayoutAnimation(mListList);
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


        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(MainActivity.this, layoutManager.getOrientation());

        mTaskList.setLayoutManager(layoutManager);
        mTaskList.setAdapter(mTaskAdapter);
        mTaskList.addItemDecoration(dividerItemDecoration);
        runLayoutAnimation(mTaskList);

    }



    /**
     * This method set ups the live data for the user
     * Which in turn set ups live data for the list
     * @param listId
     * @param userId
     * @param isFirstTime
     */
    private void setupActivity(final int listId, int userId,
                               final boolean isFirstTime) {

        setupSingleUserLiveData(listId, userId, isFirstTime);

    }


    /**
     * This method set ups live data for the user
     * @param listId
     * @param userId
     * @param isFirstTime
     */
    private void setupSingleUserLiveData(final int listId, final int userId,
                                         final  boolean isFirstTime) {

        final LiveData<User> userLiveData = userRepository.getUser(userId);

        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                userLiveData.removeObservers(MainActivity.this);
                setupSingleListLiveData(user, listId, isFirstTime);

            }
        });

    }

    /**
     * This method set ups live data for the list
     * @param listId
     * @param isFirstTime
     */
    private void setupSingleListLiveData(final User user, int listId,
                                         final boolean isFirstTime) {

        final LiveData<List> listLiveData = listRepository.getList(listId);

        listLiveData.observe(MainActivity.this, new Observer<List>() {
            @Override
            public void onChanged(List list) {

                listLiveData.removeObservers(MainActivity.this);
                setupCurrentListAndUser( user, list, isFirstTime);


            }
        });

    }

    /**
     * This method is delegated the work for
     * setting up when the data is ready.
     * @param user
     * @param list
     * @param isFirstTime
     */
    private void setupCurrentListAndUser(User user, List list,
                                         boolean isFirstTime) {

        currentUser = user;
        currentList = list;

        if (isFirstTime) {

            allTasks = new ArrayList<>();
            allLists = new ArrayList<>();
            allLists.add(currentList);

            final SharedPreferences preferences = getApplicationContext()
                    .getSharedPreferences("my_file", MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getResources()
                    .getString(R.string.is_first_time_install), false);
            editor.apply();
        }


        retrieveLists(currentUser.getUserId());
        retrieveTasks(currentList.getListId(), false);

        if (currentList != null) {
            myToolbar.setTitle(currentList.getListName());
            myToolbar.setTitleTextAppearance(MainActivity.this,
                    R.style.TextAppearance_AppCompat_Display1);
            myToolbar.setTitleTextColor(getResources()
                    .getColor(android.R.color.black));

        }
        else {

            //TODO 8: Finish the application gracefully
            //finish();
        }

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

        mBottomSheetFragment = new MainActivityBottomSheetFragment();

        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.current_list), String.valueOf(currentList.getListId()));

        mBottomSheetFragment.setArguments(bundle);
        mBottomSheetFragment.show(getSupportFragmentManager(), mBottomSheetFragment.getTag());
    }




    private void retrieveTasks(final int listId, final boolean isCompleted) {


        final LiveData<java.util.List<Task>> tasksLiveData =
                taskRepository.getAllTasks(listId, isCompleted);

        tasksLiveData.observe(this, new Observer<java.util.List<Task>>() {
            @Override
            public void onChanged(final java.util.List<Task> tasks) {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            onTaskRetrieved(isCompleted, tasks);

                        }
                    });
            }
        });

    }



    /**
     *
     * @param isCompleted
     * @param tasks
     */
    private void onTaskRetrieved(Boolean isCompleted, java.util.List<Task> tasks) {

        allTasks = tasks;

        if( allTasks != null && allTasks.size() != 0 ) {
            mTaskList.setVisibility(View.VISIBLE);
            mTaskAdapter.updateTasksData(allTasks);
            mTaskAdapter.updateUser(currentUser);
            mNoTasksPresentLayout.setVisibility(View.GONE);

        } else {

            mTaskList.setVisibility(View.GONE);
            mNoTasksPresentLayout.setVisibility(View.VISIBLE);
            //onCompletedTaskPresent();
        }
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

        //saveLastListAndUser();

    }

    private void saveLastListAndUser() {
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

        //setupCurrentListAndUser(currentUser, currentList,
        //        false);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onListClick(final List list) {


        setupCurrentListAndUser(currentUser, list, false);
        if(bottomSheet != null)
            if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

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
                false);
        saveLastListAndUser();

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

            case R.id.action_list_update: {

                UpdateListActivity.init(MainActivity.this);

                Parcelable parcelable = Parcels.wrap(currentList);
                Intent intent = new Intent(MainActivity.this, UpdateListActivity.class);
                intent.putExtra("current_list", parcelable);

                startActivity(intent);

                break;
            }

            case R.id.action_completed_tasks: {

                Intent intent = new Intent(MainActivity.this, CompletedTaskActivity.class);

                Parcelable parcel = Parcels.wrap(currentUser);
                intent.putExtra(getResources().getString(R.string.current_user), parcel);

                Parcelable parcelable = Parcels.wrap(currentList);
                intent.putExtra(getResources().getString(R.string.current_list), parcelable);

                startActivity(intent);

                break;
            }

            case R.id.action_add_widget: {

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds( new ComponentName(getContext(), MyGoalsWidget.class));

                if(appWidgetIds == null || appWidgetIds.length == 0) {

                    Snackbar.make(mParentLayout, "No Widget created on screen. First Create a Widget", Snackbar.LENGTH_LONG).show();

                    break;

                }
                else {
                    ArrayList<Task> newList = new ArrayList<>();
                    newList.addAll(allTasks);
                    MyGoalsWidget.setUpData(currentUser, currentList, newList,MainActivity.this);
                    Snackbar.make(mParentLayout, "Your tasks are now stored on the widget.", Snackbar.LENGTH_LONG).show();
                }



                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListUpdate(List list) {

        setupCurrentListAndUser(currentUser, list,
                false);
        saveLastListAndUser();

    }



    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    @Override
    public void newListCallBack(List list) {
        onListClick(list);
    }

    @Override
    public void updateList() {
        setupCurrentListAndUser(currentUser, currentList, false);
    }
}
