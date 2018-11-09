package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;
import com.udafil.dhruvamsharma.udacity_capstone.repository.CommonRepository;

import org.parceler.Parcels;

public class UpdateTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        setUpActivity();

    }

    private void setUpActivity() {

        Intent intent = getIntent();

        if(intent.hasExtra("current_task")) {

            updateTask(intent);

        } else {
            //TODO 1: Add a nice error state
            //finish();
        }



    }

    private void updateTask(Intent intent) {

        final TextInputEditText updatedText = findViewById(R.id.update_activity_edit_task_et);
        final Task task = Parcels.unwrap(intent.getParcelableExtra("current_task"));
        updatedText.setText(task.getTaskDescription());

        MaterialButton updateButton = findViewById(R.id.update_activity_update_task_btn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                task.setTaskDescription(updatedText.getText().toString());
                CommonRepository.getCommonRepository(UpdateTaskActivity.this).updateTask(task);
                finish();
            }
        });
    }
}
