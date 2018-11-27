package com.onearticleoneweek.wahadatkashmiri.firstentrymodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FirstTimeInstallActivivity extends AppCompatActivity {

    private static StartAppInterface startAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_install_activivity);




    }



    public void startApp(View view) {

        if(getIntent().hasExtra(getResources().getString(R.string.is_first_time_install)) &&
                getIntent().hasExtra(getResources().getString(R.string.current_user))&&
                getIntent().hasExtra(getResources().getString(R.string.current_list))) {

            Intent intent = getIntent();


            startAppInterface.start(intent.getIntExtra(getResources().getString(R.string.current_list), -1),
                    intent.getIntExtra(getResources().getString(R.string.current_user), -1),
                    intent.getBooleanExtra(getResources().getString(R.string.is_first_time_install), true));

        }

    }

    public static void init(Context context) {
        startAppInterface = (StartAppInterface) context;
    }

    public interface StartAppInterface {
        void start(int intExtra, int extra, boolean booleanExtra);
    }
}
