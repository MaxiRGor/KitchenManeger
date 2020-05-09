package com.distinct.kitchenmanager.model.database.database;


public class FirestoreSource {
    private static FirestoreDatabase instance;

    public static FirestoreDatabase getInstance() {
        if (instance == null)
            synchronized (FirestoreDatabase.class) {
                if (instance == null) {
                    instance = new FirestoreDatabase();
                }
            }
        return instance;
    }
}

