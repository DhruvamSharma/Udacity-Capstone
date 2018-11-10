package com.udafil.dhruvamsharma.udacity_capstone.helper;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

class MainThreadExecutor implements Executor {

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {

        mainThreadHandler.post(runnable);

    }
}
