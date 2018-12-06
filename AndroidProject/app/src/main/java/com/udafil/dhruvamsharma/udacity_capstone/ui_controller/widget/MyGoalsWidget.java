package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.User;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.ListRepository;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.UserRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.MainActivity;
import com.udafil.dhruvamsharma.udacity_capstone.ui_controller.SplashScreen;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/**
 * Implementation of App Widget functionality.
 */
public class MyGoalsWidget extends AppWidgetProvider {

    private static UserRepository userRepository;
    private static int userId = -1;
    private static User currentUser;
    private static List currentList;
    private static ArrayList<Task> currentTasks;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_goals_widget);



        setUpWidget(context, views, appWidgetId);

        Intent startActivityIntent = new Intent(context, SplashScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, 0);

        views.setOnClickPendingIntent(R.id.widget_app_icon_iv, pendingIntent);

        // The empty view is displayed when the collection has no items.
        // It should be in the same layout used to instantiate the RemoteViews
        // object above.
        //rv.setEmptyView(R.id.stack_view, R.id.empty_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        setUpData(currentUser, currentList, currentTasks, context);
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
    private static void setUpWidget(final Context context, final RemoteViews views, int appWidgetId) {

        if(currentUser != null && currentList != null && currentTasks != null) {
            //Setting up list
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(context.getResources().getString(R.string.current_list), currentTasks);

            intent.putExtra(context.getResources().getString(R.string.current_list), currentList.getListId());

            views.setRemoteAdapter(R.id.widget_list_lv, intent);

            views.setTextViewText(R.id.widget_list_name_tv, currentList.getListName());
        }

    }

    /**
     * This methods sets the userId
     * for getting the lists
     * @param user
     * @param list
     * @param context
     */
    public static void setUpData(User user, List list, ArrayList<Task> tasks, Context context) {

        userRepository = UserRepository.getUserRepository(context);
        currentUser = user;
        currentList = list;
        currentTasks = tasks;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds( new ComponentName(context, MyGoalsWidget.class));

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }


    }
}

