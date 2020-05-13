package com.distinct.kitchenmanager.model.database.database;

import android.util.Log;

import com.distinct.kitchenmanager.model.database.dao.IngredientDaoImpl;

public class FirestoreDatabase {

    FirestoreDatabase() {
        this.ingredientDao = new IngredientDaoImpl();
    }

    public IngredientDaoImpl ingredientDao;


}
