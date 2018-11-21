package com.udafil.dhruvamsharma.udacity_capstone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udafil.dhruvamsharma.udacity_capstone.database.domain.List;
import com.udafil.dhruvamsharma.udacity_capstone.helper.AppExecutor;
import com.udafil.dhruvamsharma.udacity_capstone.repository.ListRepository;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class MyGoalsWidget extends AppWidgetProvider {

    private static ListRepository listRepository;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_goals_widget);

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
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void setUpWidget(Context context, final RemoteViews views) {




    }
}

