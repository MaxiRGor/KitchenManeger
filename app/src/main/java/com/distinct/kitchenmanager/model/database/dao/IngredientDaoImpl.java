package com.distinct.kitchenmanager.model.database.dao;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.ui.MainActivity;
import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientDaoImpl implements IngredientDao {

    @Override
    public Query getIngredientsByStageTypes(int[] stageTypes) {

        return collectionReference()
                .whereIn(getString(R.string.firestore_field_ingredient_stage_type), getListFromArray(stageTypes));
    }

    @Override
    public CollectionReference getAll() {
        return collectionReference();
    }

    @Override
    public DocumentReference getById(String id) {
        return collectionReference().document(id);
    }

    @Override
    public void insert(Ingredient ingredient) {
        DocumentReference documentReference = collectionReference().document();
        ingredient.id = documentReference.getId();
        documentReference.set(getIngredientHashMap(ingredient));
    }

    @Override
    public void update(Ingredient ingredient) {
        collectionReference().document(ingredient.id).update(getIngredientHashMap(ingredient));
    }

    @Override
    public void delete(Ingredient ingredient) {
        collectionReference().document(ingredient.id).delete();
    }

    private String getString(int resId) {
        return ApplicationContextSingleton.getInstance().getApplicationContext().getString(resId);
    }

    private List<Integer> getListFromArray(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int i : array)
            list.add(i);
        return list;
    }

    private CollectionReference collectionReference() {
        return FirebaseFirestore.getInstance()
                .collection(getString(R.string.firestore_collection_fridges))
                .document(MainActivity.getFridgeId())
                .collection(getString(R.string.firestore_collection_ingredients));
    }

    private Map<String, Object> getIngredientHashMap(Ingredient ingredient) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(getString(R.string.firestore_field_ingredient_id), ingredient.id);
        hashMap.put(getString(R.string.firestore_field_ingredient_name), ingredient.name);
        hashMap.put(getString(R.string.firestore_field_ingredient_manufacturer), ingredient.manufacturer);
        hashMap.put(getString(R.string.firestore_field_ingredient_weight_type), ingredient.weightType);
        hashMap.put(getString(R.string.firestore_field_ingredient_category), ingredient.category);
        hashMap.put(getString(R.string.firestore_field_ingredient_amount_of_distinct), ingredient.amountOfDistinct);
        hashMap.put(getString(R.string.firestore_field_ingredient_amount_df_ingredients), ingredient.amountOfIngredients);
        hashMap.put(getString(R.string.firestore_field_ingredient_full_amount), ingredient.fullAmount);
        hashMap.put(getString(R.string.firestore_field_ingredient_calories_in_distinct), ingredient.caloriesInDistinct);
        hashMap.put(getString(R.string.firestore_field_ingredient_comment), ingredient.comment);
        hashMap.put(getString(R.string.firestore_field_ingredient_stage_type), ingredient.stageType);
        hashMap.put(getString(R.string.firestore_field_ingredient_shelf_life_time), ingredient.shelfLifeTime);
        return hashMap;
    }

}
