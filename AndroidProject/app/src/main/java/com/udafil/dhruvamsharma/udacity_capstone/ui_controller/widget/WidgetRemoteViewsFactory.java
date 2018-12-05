package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.Task;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper.AppExecutor;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.repository.TaskRepository;
import com.udafil.dhruvamsharma.udacity_capstone.R;

import java.util.ArrayList;
import java.util.List;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context = null;
    private int appWidgetId;
    private List<Task> tasks;
    private int mListId;
    private TaskRepository taskRepository;


    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {

        this.context = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("AppWidgetId", String.valueOf(appWidgetId));

        if(intent.hasExtra(context.getResources().getString(R.string.current_list)))
        mListId = intent.getIntExtra(context.getResources().getString(R.string.current_list), 0);

        taskRepository = TaskRepository.getCommonRepository(context);

    }

    @Override
    public void onCreate() {

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                tasks = taskRepository.getAllTasksWithoutLiveData(mListId, false);

            }
        });

    }

    private void setupData() {

        // For inintialization purpose
        tasks = new ArrayList<>();





    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

        tasks.clear();

    }

    @Override
    public int getCount() {

        return tasks.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_main_single_list_layout);
        Log.d("WidgetCreatingView", tasks.get(i).getTaskDescription());
        remoteViews.setTextViewText(R.id.list_layout_text_main_activity_list_tv, tasks.get(i).getTaskDescription());
        return  remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
