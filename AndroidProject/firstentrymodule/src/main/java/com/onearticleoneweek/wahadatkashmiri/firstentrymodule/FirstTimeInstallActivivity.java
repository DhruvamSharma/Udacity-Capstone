package com.onearticleoneweek.wahadatkashmiri.firstentrymodule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class FirstTimeInstallActivivity extends AppIntro2 {

    private static StartAppInterface startAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_first_time_install_activivity);

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle(getResources().getString(R.string.fragment_one_title));
        sliderPage3.setDescription(getResources().getString(R.string.fragment_one_desc));
        sliderPage3.setImageDrawable(R.drawable.no_tasks_white);
        sliderPage3.setBgColor(getResources().getColor(android.R.color.background_light));

        sliderPage3.setDescColor(getResources().getColor(android.R.color.background_dark));
        sliderPage3.setTitleColor(getResources().getColor(android.R.color.background_dark));

        addSlide(AppIntro2Fragment.newInstance(sliderPage3));


        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle(getResources().getString(R.string.fragment_two_title));
        sliderPage2.setDescription(getResources().getString(R.string.fragment_two_desc));
        sliderPage2.setImageDrawable(R.drawable.score_points);
        sliderPage2.setBgColor(getResources().getColor(android.R.color.background_light));

        sliderPage2.setDescColor(getResources().getColor(android.R.color.background_dark));
        sliderPage2.setTitleColor(getResources().getColor(android.R.color.background_dark));

        addSlide(AppIntro2Fragment.newInstance(sliderPage2));

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle(getResources().getString(R.string.fragment_three_title));
        sliderPage1.setDescription(getResources().getString(R.string.fragment_three_desc));
        sliderPage1.setImageDrawable(R.drawable.no_login);
        sliderPage1.setBgColor(getResources().getColor(android.R.color.background_light));

        sliderPage1.setDescColor(getResources().getColor(android.R.color.background_dark));
        sliderPage1.setTitleColor(getResources().getColor(android.R.color.background_dark));

        addSlide(AppIntro2Fragment.newInstance(sliderPage1));

        setImmersiveMode(true);
        showSkipButton(false);
        //setProgressButtonEnabled(false);

        setBarColor(getResources().getColor(android.R.color.background_light));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);

        setIndicatorColor(getResources().getColor(R.color.indicator_color_light), getResources().getColor(android.R.color.darker_gray));

        //setFadeAnimation();
    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        startApp();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }



    public void startApp() {

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
