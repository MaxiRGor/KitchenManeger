package com.distinct.kitchenmanager.model.database.dao;

import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;


//@Dao
public interface IngredientDao {
    //todo realtime updates
    // @Query("SELECT * FROM Ingredient where stage_type in (:stageTypes)")
    Query getIngredientsByStageTypes(int[] stageTypes);

    //  //todo read once (ande below)
    //  @Query("SELECT * FROM Ingredient where stage_type in (:stageTypes) and name like '%' || :search || '%'")
   // Query getIngredientsByStageTypesAndName(int[] stageTypes, String search);

    //  @Query("SELECT * FROM Ingredient")
    CollectionReference getAll();

    // @Query("SELECT * FROM Ingredient where id = (:id)")
    DocumentReference getById(String id);

    //   @Insert
    void insert(Ingredient ingredient);

    //   @Update
    void update(Ingredient ingredient);

    //    @Delete
    void delete(Ingredient ingredient);

}
