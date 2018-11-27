package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class MyGoalsWidget extends AppWidgetProvider {

    private static ListRepository listRepository;
    private static int userId = -1;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int userId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_goals_widget);

        listRepository = ListRepository.getCommonRepository(context);

        setUpWidget(context, views);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_new_task_et, pendingIntent);

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
    private static void setUpWidget(Context context, final RemoteViews views) {

        final StringBuilder data = new StringBuilder();

        if(userId == -1) {
            userId = 0;
        }

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                java.util.List<List> lists = listRepository.getListsWithoutLiveData(userId);

                if( lists != null) {
                    for (List list: lists) {

                        data.append(list.getListName());

                    }

                    AppExecutor.getsInstance().getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            views.setTextViewText(R.id.widget_new_task_et, new String(data));
                            Log.e("TAG_ERROR" ,new String(data));
                        }
                    });
                } else {

                }

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

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds( new ComponentName(context, MyGoalsWidget.class));

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, userId);
        }


    }
}

