package com.mairos.ymoneytransfer.network;

import android.util.Log;

import com.octo.android.robospice.UncachedSpiceService;

import roboguice.util.temp.Ln;

public class YmoneySpiceService extends UncachedSpiceService {

    @Override
    public void onCreate() {
        Ln.getConfig().setLoggingLevel(Log.ERROR);
        super.onCreate();
    }

    @Override
    public int getThreadCount() {
        return 4;
    }

    @Override
    public int getThreadPriority() {
        return Thread.MAX_PRIORITY;
    }
}
