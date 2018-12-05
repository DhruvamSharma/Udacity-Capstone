package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }
}
