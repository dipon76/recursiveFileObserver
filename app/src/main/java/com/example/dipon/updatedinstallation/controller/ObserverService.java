package com.example.dipon.updatedinstallation.controller;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

/**
 * Created by Dipon on 4/18/2017.
 */

public class ObserverService extends Service {
    private String path = Environment.getExternalStorageDirectory().getPath()+"/";

    private RecursiveFileObserver observer = new RecursiveFileObserver(path, this);
    private String TAG = "ObserverService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        observer.startWatching();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Log.d(TAG, "onStartCommand: started");
        return ObserverService.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observer.stopWatching();
    }

    void getList()
    {
        File file = new File (path);
        File []list = file.listFiles();
        Log.d(TAG, "getList: we can read " +list.length);
    }
}
