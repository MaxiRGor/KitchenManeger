package com.distinct.kitchenmanager.model.database.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.distinct.kitchenmanager.model.database.dao.ConsumedDao;
import com.distinct.kitchenmanager.model.database.dao.IngredientDao;
import com.distinct.kitchenmanager.model.database.entity.Consumed;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;

@Database(entities = {Consumed.class}, version = 1)
public abstract class RoomAppDatabase extends RoomDatabase {
    public abstract ConsumedDao consumedDao();
}
