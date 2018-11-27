package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private View viewForSnackBar;

    public MainActivityTaskListAdapter(Context context, View view) {

        contextWeakReference = new WeakReference<>(context);
        viewForSnackBar = view;

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
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {

        taskViewHolder.taskTextView.setText(tasks.get(i).getTaskDescription());

        if(tasks.get(i).getComlpleted()) {
            taskViewHolder.animationView.setProgress(1f);
        } else {
            taskViewHolder.animationView.setProgress(0f);
        }


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
        private LottieAnimationView animationView;

        public TaskViewHolder(@NonNull final View itemView) {
            super(itemView);

            taskTextView = itemView.findViewById(R.id.task_layout_text_main_activity_task_list_tv);
            animationView = itemView.findViewById(R.id.select_task_rg);

            animationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startCheckAnimation(animationView, tasks.get(getAdapterPosition()) );
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    upDateTask( tasks.get(getAdapterPosition()) );

                }
            });
        }
    }

    private void upDateTask(Task task) {

        Parcelable parcelable = Parcels.wrap(task);

        Intent intent = new Intent(contextWeakReference.get(), UpdateTaskActivity.class);

        intent.putExtra("current_task", parcelable);

        contextWeakReference.get().startActivity(intent);

    }

    public void updateTasksData(List<Task> tasks) {

        this.tasks = tasks;
        notifyDataSetChanged();

    }

    private void completeTask(final LottieAnimationView animationView, final Task task) {

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

                        animationView.setProgress(1f);

                        tasks.remove(task);

                        Toast.makeText(contextWeakReference.get(), "Task Completed, you total points: " +
                                currentUser.getScore(), Toast.LENGTH_SHORT).show();

//                        //TODO ERROR: add another view. The view gets removed before the snackbar appears
//                        Snackbar.make( animationView,"Task Completed, you total points: " +
//                                currentUser.getScore(), Snackbar.LENGTH_LONG).show();

                        updateTasksData(tasks);



                    }
                });

            }
        });

    }


    private void startCheckAnimation(final LottieAnimationView animationView, Task task) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animationView.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

        if (animationView.getProgress() == 0f) {
            animator.start();

            completeTask(animationView, task);

        } else {
            animationView.setProgress(0f);

            inCompleteATask(animationView, task);


        }
    }

    private void inCompleteATask(final LottieAnimationView animationView, final Task task) {

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

                        animationView.setProgress(0f);

                        tasks.remove(task);

//                        //TODO ERROR: add another view. The view gets removed before the snackbar appears
//                        Snackbar.make( animationView, "Task incomplete, you total points: " +
//                                currentUser.getScore(), Snackbar.LENGTH_LONG).show();

                        updateTasksData(tasks);

                        Toast.makeText(contextWeakReference.get(), "Task inComplete, you total points: " +
                                currentUser.getScore(), Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });

    }
}
