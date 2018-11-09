package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;
import com.udafil.dhruvamsharma.udacity_capstone.repository.CommonRepository;

import java.util.Date;

public class NewTaskBottomSheet extends AppCompatActivity {

    private String mTaskDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_bottom_sheet);

        final FloatingActionButton saveTask = findViewById(R.id.main_activity_bottom_sheet_save_task_fab);

        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveTask();

            }
        });
    }


    private void saveTask() {

        TextInputEditText newTask = findViewById(R.id.main_activity_bottom_sheet_edit_task_et);
        if(newTask.getText() != null)
            mTaskDescription = newTask.getText().toString();

        Task task = new Task(mTaskDescription, false, 1, new Date());

        CommonRepository repository = CommonRepository.getCommonRepository(getApplicationContext());
        repository.insertTask(task);
    }
}
