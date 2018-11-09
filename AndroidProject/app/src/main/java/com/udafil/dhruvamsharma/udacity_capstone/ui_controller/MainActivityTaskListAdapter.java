package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivityTaskListAdapter extends RecyclerView.Adapter<MainActivityTaskListAdapter.TaskViewHolder> {

    private List<Task> tasks;

    public MainActivityTaskListAdapter() {

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

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTextView = itemView.findViewById(R.id.task_layout_text_main_activity_task_list_tv);
        }
    }

    public void updateTasks(List<Task> tasks) {

        this.tasks = tasks;
        notifyDataSetChanged();

    }
}
