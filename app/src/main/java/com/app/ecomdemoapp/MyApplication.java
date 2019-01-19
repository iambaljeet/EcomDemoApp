package com.app.ecomdemoapp;

import android.app.Application;
import android.content.Context;

import com.app.ecomdemoapp.database.SqliteHelper;

public class MyApplication extends Application {
    private Context context;
    private static SqliteHelper sqliteHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        sqliteHelper = new SqliteHelper(context);
    }

    public Context getContext() {
        return context;
    }

    public static SqliteHelper getSqliteHelper() {
        return sqliteHelper;
    }
}
