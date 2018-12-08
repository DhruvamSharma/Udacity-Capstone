package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.MainActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list.UpdateListActivity;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivityTaskListAdapter extends
        RecyclerView.Adapter<MainActivityTaskListAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private WeakReference<Context> contextWeakReference;
    private User currentUser;
    private UpdateCallbacks mCallbacks;

    public MainActivityTaskListAdapter(Context context) {

        contextWeakReference = new WeakReference<>(context);

        //mCallbacks = (UpdateCallbacks) context;
        this.tasks = new ArrayList<>();
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View holder = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.activity_main_single_task_layout, viewGroup, false);

        return new TaskViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder taskViewHolder, final int i) {

        taskViewHolder.taskTextView.setText(tasks.get(i).getTaskDescription());

        taskViewHolder.animationView.setChecked(false);

        if(tasks.get(taskViewHolder.getAdapterPosition()).getComlpleted()) {
            taskViewHolder.animationView.setChecked(true);
            taskViewHolder.taskTextView.setPaintFlags(taskViewHolder.taskTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {

            taskViewHolder.animationView.setChecked(false);
            taskViewHolder.taskTextView.setPaintFlags(taskViewHolder.taskTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }

        taskViewHolder.animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCheckAnimation(taskViewHolder.animationView, tasks.get(i) );
            }
        });

        taskViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upDateTask( tasks.get(i) );

            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateUser(User currentUser) {

        this.currentUser = currentUser;

    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskTextView;
        private CheckBox animationView;

        public TaskViewHolder(@NonNull final View itemView) {
            super(itemView);

            taskTextView = itemView.findViewById(R.id.task_layout_text_main_activity_task_list_tv);
            animationView = itemView.findViewById(R.id.select_task_rg);


        }
    }

    private void upDateTask(Task task) {

        Parcelable parcelable = Parcels.wrap(task);

        Intent intent = new Intent(contextWeakReference.get(), UpdateTaskActivity.class);

        intent.putExtra(contextWeakReference.get().getResources().getString(R.string.current_task), parcelable);

        contextWeakReference.get().startActivity(intent);

    }

    public void updateTasksData(List<Task> tasks) {

        this.tasks = tasks;
        notifyDataSetChanged();

    }

    private void completeTask(final CheckBox animationView, final Task task) {

        task.setComlpleted(true);

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                TaskRepository
                        .getCommonRepository(contextWeakReference.get())
                        .updateTask(task);

                currentUser
                        .setScore(currentUser.getScore() + contextWeakReference.get()
                                .getResources().getInteger(R.integer.score_on_task_complete));

                UserRepository.getUserRepository(contextWeakReference.get())
                        .updateUser(currentUser);

                AppExecutor.getsInstance().getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(contextWeakReference.get(), "Task Completed, you total points: " +
                                currentUser.getScore(), Toast.LENGTH_SHORT).show();

                        animationView.setChecked(true);

                    }
                });

            }
        });

    }


    private void startCheckAnimation(final CheckBox animationView, Task task) {


        if (!task.getComlpleted()) {

            completeTask(animationView, task);

        } else {

            inCompleteATask(animationView, task);


        }
    }

    private void inCompleteATask(final CheckBox animationView, final Task task) {

        task.setComlpleted(false);

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                TaskRepository
                        .getCommonRepository(contextWeakReference.get())
                        .updateTask(task);

                currentUser
                        .setScore(currentUser.getScore() - contextWeakReference.get()
                                .getResources().getInteger(R.integer.score_on_task_complete));

                UserRepository.getUserRepository(contextWeakReference.get())
                        .updateUser(currentUser);

                AppExecutor.getsInstance().getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        animationView.setChecked(false);

                        Toast.makeText(contextWeakReference.get(), "Task inComplete now, you total points: " +
                                currentUser.getScore(), Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });

    }


    public interface UpdateCallbacks {

        void updateList();

    }



}
