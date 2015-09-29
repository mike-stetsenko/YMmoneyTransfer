package com.mairos.ymoneytransfer;

import android.app.Application;

public class YmoneyApplication extends Application {

    private static YmoneyApplication sInstance;

    public static YmoneyApplication getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
