package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.MainActivity;

import org.parceler.Parcels;

public class UpdateTaskActivity extends AppCompatActivity {

    Toolbar myToolbar;

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

        //Toolbar Setup
        myToolbar = findViewById(R.id.my_toolbar_update_tasks);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();

        if(intent.hasExtra(getResources().getString(R.string.current_task))) {

            Task task = Parcels.unwrap(intent
                    .getParcelableExtra(getResources()
                            .getString(R.string.current_task)));

            updateToolbar(task.getListId());
            update(task);

        } else {
            //TODO 1: Add a nice error state
            //finish();
            //Toast.makeText(this, "task not updated", Toast.LENGTH_SHORT).show();
        }



    }

    private void updateToolbar(final int listId) {


        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List list = ListRepository
                        .getCommonRepository(UpdateTaskActivity.this)
                        .getSingleListWithoutLiveData(listId);
                AppExecutor.getsInstance().getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(list != null) {

                            myToolbar.setTitle(list.getListName());
                        } else {

                            myToolbar.setTitle(getResources().getString(R.string.app_name));
                        }

                        myToolbar.setTitleTextAppearance(UpdateTaskActivity.this,
                                R.style.TextAppearance_AppCompat_Display1);
                        myToolbar.setTitleTextColor(getResources()
                                .getColor(android.R.color.black));

                    }
                });
            }
        });



    }

    private void update(final Task task) {

        final TextInputEditText updatedText = findViewById(R.id.update_activity_edit_task_et);

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

        //Toast.makeText(UpdateTaskActivity.this, updatedText.getText().toString(), Toast.LENGTH_SHORT).show();

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                task.setTaskDescription(updatedText.getText().toString());
                final boolean updated = TaskRepository.getCommonRepository(UpdateTaskActivity.this).updateTask(task);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(updated)
                            finish();
                        else
                            Toast.makeText(UpdateTaskActivity.this, updatedText.getText().toString(), Toast.LENGTH_SHORT).show();

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
