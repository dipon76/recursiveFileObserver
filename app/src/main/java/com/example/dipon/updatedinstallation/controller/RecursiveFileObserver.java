package com.example.dipon.updatedinstallation.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Dipon on 4/18/2017.
 */

public class RecursiveFileObserver extends FileObserver {

    public static int CHANGES_ONLY = CLOSE_WRITE | MOVE_SELF | MOVED_FROM;

    private String TAG = "DOWNLOAD_OBSERVER";
    List<SingleFileObserver> mObservers;
    String mPath;
    int mMask;

    private Context serviceContext;

    public RecursiveFileObserver(String path , Context context) {
        this(path, ALL_EVENTS,context);
    }

    public RecursiveFileObserver(String path, int mask , Context context) {
        super(path, mask);
        mPath = path;
        mMask = mask;
        serviceContext = context;
    }

    @Override
    public void startWatching() {
        if (mObservers != null) return;
        mObservers = new ArrayList<SingleFileObserver>();
        Stack<String> stack = new Stack<String>();

        stack.push(mPath);
        Log.d(TAG, "startWatching: " + mPath);

        while (!stack.empty()) {
            String parent = stack.pop();
            Log.d(TAG, "startWatching: "+parent);
            mObservers.add(new SingleFileObserver(parent, mMask));
            File path = new File(parent);
            File[] files = path.listFiles();
            if (files == null) continue;
            for (int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory() && !files[i].getName().equals(".")
                        && !files[i].getName().equals("..")) {
                    stack.push(files[i].getPath());
                }
            }
        }
        for (int i = 0; i < mObservers.size(); i++)
            mObservers.get(i).startWatching();
    }

    @Override
    public void stopWatching() {
        if (mObservers == null) return;

        for (int i = 0; i < mObservers.size(); ++i)
            mObservers.get(i).stopWatching();

        mObservers.clear();
        mObservers = null;
    }

    @Override
    public void onEvent(int event, String path) {
        switch (event) {
            case FileObserver.ACCESS:
                Log.i(TAG, "ACCESS: " + path);
                break;
            case FileObserver.ATTRIB:
                Log.i(TAG, "ATTRIB: " + path);
                break;
            case FileObserver.CLOSE_NOWRITE:
                Log.i(TAG, "CLOSE_NOWRITE: " + path);
                break;
            case FileObserver.CLOSE_WRITE:
                Log.i(TAG, "CLOSE_WRITE: " + path);
                break;
            case FileObserver.CREATE:
                Log.i(TAG, "CREATE: " + path);
                break;
            case FileObserver.DELETE:
                Log.i(TAG, "DELETE: " + path);
                break;
            case FileObserver.DELETE_SELF:
                Log.i(TAG, "DELETE_SELF: " + path);
                break;
            case FileObserver.MODIFY:
                Log.i(TAG, "MODIFY: " + path);
                break;
            case FileObserver.MOVE_SELF:
                Log.i(TAG, "MOVE_SELF: " + path);
                break;
            case FileObserver.MOVED_FROM:
                Log.i(TAG, "MOVED_FROM: " + path);
                break;
            case FileObserver.MOVED_TO:
                Log.i(TAG, "MOVED_TO: " + path);
                Log.d(TAG, "onEvent: launching apk");
                openApk(path);
                break;
            case FileObserver.OPEN:
                Log.i(TAG, "OPEN: " + path);
                break;
            default:
                Log.i(TAG, "DEFAULT(" + event + "): " + path);
                break;
        }
    }

    private class SingleFileObserver extends FileObserver {
        private String mPath;

        public SingleFileObserver(String path, int mask) {
            super(path, mask);
            mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            String newPath = mPath + "/" + path;
            // Log.d(TAG, "onEvent: "+newPath);
            RecursiveFileObserver.this.onEvent(event, newPath);
        }

    }

    public void openApk(String path) {
        if(path.endsWith(".apk")) {
            Intent intent = new Intent(serviceContext.getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("path",path);
            Log.d(TAG, "openApk: "+ path);
            serviceContext.startActivity(intent);
        }
    }

}
