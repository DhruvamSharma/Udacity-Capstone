package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;

import org.parceler.Parcels;

public class UpdateListActivity extends AppCompatActivity {

    private UpdateListCallBacks mUpdateListCallBacks;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list);

        setUpActivity();
    }

    private void setUpActivity() {

        Intent intent = getIntent();
        mUpdateListCallBacks = (UpdateListCallBacks) mContext;

        if(intent.hasExtra("current_list")) {

            updateList(intent);

        } else {
            //TODO 1: Add a nice error state
            //finish();
        }

    }

    public static void init( Context context) {
        mContext = context;
    }

    private void updateList(Intent intent) {

        final TextInputEditText updatedText = findViewById(R.id.update_activity_edit_list_et);
        final List list = Parcels.unwrap(intent.getParcelableExtra("current_list"));
        updatedText.setText(list.getListName());

        MaterialButton updateButton = findViewById(R.id.update_activity_update_list_btn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String listName = updatedText.getText().toString();
                list.setListName(listName);

                new MyClassAsyncTask().execute(list);

            }
        });
    }

    public interface UpdateListCallBacks {
        void onListUpdate(List list);
    }


    private class MyClassAsyncTask extends AsyncTask<List, Void, List> {

        @Override
        protected List doInBackground(List... lists) {

            ListRepository.getCommonRepository(UpdateListActivity.this).updateList(lists[0]);

            return lists[0];
        }

        @Override
        protected void onPostExecute(List response) {
            super.onPostExecute(response);

            mUpdateListCallBacks.onListUpdate(response);
            finish();

        }
    }
}
