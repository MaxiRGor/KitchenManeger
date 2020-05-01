package com.distinct.kitchenmanager.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.distinct.kitchenmanager.model.room.entity.Ingredient;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface IngredientDao {
    @Query("SELECT * FROM Ingredient where stage_type in (:stageTypes)")
    LiveData<List<Ingredient>> getAllForShoppingListLiveData(int[] stageTypes);

    @Query("SELECT * FROM Ingredient where stage_type in (:stageTypes) and name like '%' || :search || '%'")
    List<Ingredient> getByNameForShoppingList(int[] stageTypes, String search);

    @Query("SELECT * FROM Ingredient")
    List<Ingredient> getAll();

    @Query("SELECT * FROM Ingredient where id = (:id)")
    Ingredient getById(int id);

    @Insert
    void insert(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Delete
    void deleteItems(ArrayList<Ingredient> ingredients);

}
