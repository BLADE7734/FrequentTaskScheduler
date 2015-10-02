package com.blade7734.frequenttaskscheduler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;


public class ScheduleFrequentTaskService extends Service {
    static final long INTERVAL = 1000;
    static final String LOG_TAG = ScheduleFrequentTaskService.class.getSimpleName();
    private volatile Looper mTaskLooper;
    private volatile Handler mTaskHandler;



    private Runnable mTaskRunnable = new Runnable() {
        @Override
        public void run() {
            doTask();
            mTaskHandler.postDelayed(this, INTERVAL);
        }
    };

    private void doTask() {
        Log.i(LOG_TAG, "doTask Message");
    }

    public static Intent createServiceIntent(Context context){
        return new Intent(context, ScheduleFrequentTaskService.class);
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "onCreate Message");
        super.onCreate();

        HandlerThread taskThread = new HandlerThread("ScheduleFrequentTaskService");
        taskThread.start();

        mTaskLooper = taskThread.getLooper();
        mTaskHandler = new Handler(mTaskLooper);
        mTaskHandler.postDelayed(mTaskRunnable, 0L);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "onStartCommand Message. startId = " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy Message");
        mTaskHandler.removeCallbacks(mTaskRunnable);
        mTaskLooper.quit();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
