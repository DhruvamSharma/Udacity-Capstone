package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

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

public class MainActivityTaskListAdapter extends RecyclerView.Adapter<MainActivityTaskListAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private WeakReference<Context> contextWeakReference;
    private User currentUser;

    public MainActivityTaskListAdapter(Context context) {

        contextWeakReference = new WeakReference<>(context);

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
        RadioGroup completeTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTextView = itemView.findViewById(R.id.task_layout_text_main_activity_task_list_tv);
            completeTask = itemView.findViewById(R.id.select_task_rg);

            completeTask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    if(i == R.id.select_task_rb) {

                        completeTask(tasks.get(getAdapterPosition()));

                    }

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

    private void completeTask(final Task task) {

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

                        tasks.remove(task);
                        updateTasksData(tasks);

                    }
                });

            }
        });

    }
}
