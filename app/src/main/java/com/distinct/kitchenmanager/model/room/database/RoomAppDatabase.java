package com.distinct.kitchenmanager.model.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.distinct.kitchenmanager.model.room.dao.ConsumedDao;
import com.distinct.kitchenmanager.model.room.dao.IngredientDao;
import com.distinct.kitchenmanager.model.room.entity.Consumed;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;

@Database(entities = {Ingredient.class, Consumed.class}, version = 1)
public abstract class RoomAppDatabase extends RoomDatabase {
    public abstract IngredientDao ingredientDao();

    public abstract ConsumedDao consumedDao();
}
