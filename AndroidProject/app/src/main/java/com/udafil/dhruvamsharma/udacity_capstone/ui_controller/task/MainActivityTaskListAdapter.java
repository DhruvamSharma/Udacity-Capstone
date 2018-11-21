package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
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

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTextView = itemView.findViewById(R.id.task_layout_text_main_activity_task_list_tv);


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
}
