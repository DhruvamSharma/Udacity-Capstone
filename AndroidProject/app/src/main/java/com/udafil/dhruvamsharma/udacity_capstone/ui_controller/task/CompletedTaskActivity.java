package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.MainActivity;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;

import org.parceler.Parcels;


public class CompletedTaskActivity extends AppCompatActivity {

    private RecyclerView mCompletedTaskList;
    //List name
    private TextView mCompletedTextLabel;
    //A common taskRepository for all the network and
    //database operations
    private TaskRepository taskRepository;
    private ListRepository listRepository;
    private UserRepository userRepository;


    private User currentUser;
    private List currentList;
    private java.util.List<Task> allTasks;

    MainActivityTaskListAdapter mCompletedTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);


        //setting up the taskRepository
        listRepository = ListRepository.getCommonRepository(CompletedTaskActivity.this);
        taskRepository = TaskRepository.getCommonRepository(CompletedTaskActivity.this);
        userRepository = UserRepository.getUserRepository(CompletedTaskActivity.this);

        if(getIntent().hasExtra(getResources().getString(R.string.current_list)) &&
                getIntent().hasExtra(getResources().getString(R.string.current_user))) {

            currentList = Parcels.unwrap(getIntent()
                    .getParcelableExtra(getResources().getString(R.string.current_list)));

            currentUser = Parcels.unwrap(getIntent()
                    .getParcelableExtra(
                    getResources().getString(R.string.current_user)));

            retrieveTasks(currentList.getListId(), true);
            setupCompletedTaskRecyclerView();
            //List Name Text View
            mCompletedTextLabel = findViewById(R.id.completed_task_text_view_tv);
        } else {
            // TODO finish gracefully
        }



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

                        Toast.makeText(CompletedTaskActivity.this, "onTaskRetrieved, retrieving from live data " + isCompleted, Toast.LENGTH_SHORT).show();

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

    private void setupCompletedTaskRecyclerView() {

        mCompletedTaskList = findViewById(R.id.task_list__completed_main_activity_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mCompletedTaskAdapter = new MainActivityTaskListAdapter(this);


        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(CompletedTaskActivity.this, layoutManager.getOrientation());

        mCompletedTaskList.setLayoutManager(layoutManager);
        mCompletedTaskList.setAdapter(mCompletedTaskAdapter);
        mCompletedTaskList.addItemDecoration(dividerItemDecoration);
        runLayoutAnimation(mCompletedTaskList);

    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
