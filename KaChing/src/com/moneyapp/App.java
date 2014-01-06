package com.moneyapp;

import android.app.Application;
import android.content.Context;

// Delete this Class - maybe
public class App extends Application {
    public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}