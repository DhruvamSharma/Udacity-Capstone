package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;

import org.parceler.Parcels;

import java.util.Date;

public class NewListActivity extends AppCompatActivity {

    private String mListName;
    private static NewListCallBacks mListCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        final Intent intent = getIntent();

        if(intent.hasExtra(getResources().getString(R.string.current_user))) {

            final MaterialButton saveList = findViewById(R.id.new_list_activity_create_list_btn);

            saveList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    saveList(intent.getIntExtra(getResources()
                            .getString(R.string.current_user), -1));

                }
            });
        }



    }

    public static void init(Context context) {
        mListCallbacks = (NewListCallBacks) context;
    }

    private void saveList(int userId) {

        TextInputEditText newList = findViewById(R.id.new_list_activity_create_list_et);
        if(newList.getText() != null && !newList.getText().toString().equals("")) {
            mListName = newList.getText().toString();


            final List list = new List(userId, mListName, new Date());

            final ListRepository repository = ListRepository.getCommonRepository(NewListActivity.this);

            AppExecutor.getsInstance()
                    .getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    repository.insertList(list);
                    AppExecutor.getsInstance().getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            mListCallbacks.newListCallBack(list);
                            finish();
                        }
                    });

                }
            });


        }

    }


    public interface NewListCallBacks {

        void newListCallBack(List list);

    }
}
