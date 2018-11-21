package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.database.domain.List;
import com.udafil.dhruvamsharma.udacity_capstone.helper.AppExecutor;
import com.udafil.dhruvamsharma.udacity_capstone.repository.ListRepository;

import org.parceler.Parcels;

public class UpdateListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list);

        setUpActivity();
    }

    private void setUpActivity() {

        Intent intent = getIntent();

        if(intent.hasExtra("current_list")) {

            updateList(intent);

        } else {
            //TODO 1: Add a nice error state
            //finish();
        }



    }

    private void updateList(Intent intent) {

        final TextInputEditText updatedText = findViewById(R.id.update_activity_edit_list_et);
        final List list = Parcels.unwrap(intent.getParcelableExtra("current_list"));
        updatedText.setText(list.getListName());

        MaterialButton updateButton = findViewById(R.id.update_activity_update_list_btn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.setListName(updatedText.getText().toString());
                AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        ListRepository.getCommonRepository(UpdateListActivity.this).updateList(list);
                        finish();
                    }
                });

            }
        });
    }
}
