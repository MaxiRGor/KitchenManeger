package com.distinct.kitchenmanager.model.room.database;

import android.content.Context;

import androidx.room.Room;

public class RoomDatabaseSource {
    private static RoomAppDatabase instance;

    public static RoomAppDatabase getInstance(Context context) {
        if (instance == null)
            synchronized (RoomAppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            RoomAppDatabase.class,
                            "application_database").build();
                }
            }
        return instance;
    }
}
