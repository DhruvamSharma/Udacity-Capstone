package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;
import com.udafil.dhruvamsharma.udacity_capstone.helper.AppExecutor;
import com.udafil.dhruvamsharma.udacity_capstone.repository.TaskRepository;

import org.parceler.Parcels;

public class UpdateTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        setUpActivity();

    }

    /**
     * This methods helps in setting up the activity
     */
    private void setUpActivity() {

        Intent intent = getIntent();

        if(intent.hasExtra("current_task")) {

            update(intent);

        } else {
            //TODO 1: Add a nice error state
            //finish();
        }



    }

    private void update(Intent intent) {

        final TextInputEditText updatedText = findViewById(R.id.update_activity_edit_task_et);
        final Task task = Parcels.unwrap(intent.getParcelableExtra("current_task"));
        updatedText.setText(task.getTaskDescription());

        final MaterialButton updateTaskButton = findViewById(R.id.update_activity_update_task_btn);
        final MaterialButton deleteTaskButton = findViewById(R.id.update_activity_delete_task_btn);

        updateTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateTask(updatedText, task);

            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteTask(task);

            }
        });



    }

    private void updateTask(final TextInputEditText updatedText,final Task task) {

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                task.setTaskDescription(updatedText.getText().toString());
                TaskRepository.getCommonRepository(UpdateTaskActivity.this).updateTask(task);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });

            }
        });


    }


    private void deleteTask(final Task task) {

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                TaskRepository.getCommonRepository(UpdateTaskActivity.this).deleteTask(task);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });

            }
        });




    }
}
