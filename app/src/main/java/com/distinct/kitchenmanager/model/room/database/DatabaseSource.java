package com.distinct.kitchenmanager.model.room.database;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

public class DatabaseSource {
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null)
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            AppDatabase.class,
                            "application_database").build();
                }
            }
        return instance;
    }
}
