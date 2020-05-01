package com.distinct.kitchenmanager.model.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.distinct.kitchenmanager.model.room.dao.IngredientDao;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;

@Database(entities = {Ingredient.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract IngredientDao ingredientDao();
}
