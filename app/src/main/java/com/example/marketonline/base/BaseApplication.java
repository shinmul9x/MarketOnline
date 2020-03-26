package com.example.marketonline.base;

import android.app.Application;

import com.example.marketonline.database.AppDatabase;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.createDatabase(this);
    }
}
