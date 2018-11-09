package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.List;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.Task;
import com.udafil.dhruvamsharma.udacity_capstone.repository.ListRepository;
import com.udafil.dhruvamsharma.udacity_capstone.repository.TaskRepository;

import java.util.Date;

public class NewListActivity extends AppCompatActivity {

    private String mListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        final MaterialButton saveList = findViewById(R.id.new_list_activity_create_list_btn);

        saveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveTask();

            }
        });

    }

    private void saveTask() {

        TextInputEditText newList = findViewById(R.id.new_list_activity_create_list_et);
        if(newList.getText() != null || newList.getText().toString().equals("")) {
            mListName = newList.getText().toString();

            //TODO 3: Change List ID
            List list = new List(1, mListName, new Date());

            ListRepository repository = ListRepository.getCommonRepository(NewListActivity.this);
            repository.insertList(list);

            finish();

        }

    }
}
