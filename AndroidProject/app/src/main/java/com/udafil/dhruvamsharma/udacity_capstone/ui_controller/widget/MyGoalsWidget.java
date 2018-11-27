package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.MainActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.SplashScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/**
 * Implementation of App Widget functionality.
 */
public class MyGoalsWidget extends AppWidgetProvider {

    private static UserRepository userRepository;
    private static int userId = -1;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int userId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_goals_widget);

        setUpWidget(context, views);

        Intent intent = new Intent(context, SplashScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_app_icon_iv, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        setUpData(userId, context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     *This methods sets the data onto the widget
     * @param context
     * @param views
     */
    private static void setUpWidget(final Context context, final RemoteViews views) {

        final StringBuilder data = new StringBuilder();

        if(userId == -1) {
            userId = 0;
        }

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final User currentUser  = userRepository.getUserWithoutLiveData(userId);
                views.setTextViewText(R.id.widget_my_points_tv, "" + currentUser.getScore());
                AppExecutor.getsInstance().getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(context, "score: "+ currentUser.getScore(), Toast.LENGTH_LONG).show();

                    }
                });

            }
        });





    }

    /**
     * This methods sets the userId
     * for getting the lists
     * @param userID
     */
    public static void setUpData(int userID, Context context) {

        userId = userID;

        userRepository = UserRepository.getUserRepository(context);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds( new ComponentName(context, MyGoalsWidget.class));

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, userId);
        }


    }
}

