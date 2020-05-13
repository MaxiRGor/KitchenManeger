package com.distinct.kitchenmanager.model.database.dao;

import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public interface IngredientDao {

    Query getIngredientsByStageTypes(int[] stageTypes);

    CollectionReference getAll();

    DocumentReference getById(String id);

    void insert(Ingredient ingredient);

    void update(Ingredient ingredient);

    void delete(Ingredient ingredient);

}
