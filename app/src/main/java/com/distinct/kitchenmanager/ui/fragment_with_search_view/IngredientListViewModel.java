package com.distinct.kitchenmanager.ui.fragment_with_search_view;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.distinct.kitchenmanager.model.database.database.FirestoreDatabase;
import com.distinct.kitchenmanager.model.database.database.FirestoreSource;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class IngredientListViewModel extends AndroidViewModel {

    public FirestoreDatabase firestoreDatabase;

    protected MutableLiveData<List<Ingredient>> items;
    private MutableLiveData<List<Ingredient>> foundItems;
    private MediatorLiveData<List<Ingredient>> itemsToShow;
    private int[] suitableStageTypes;


    public IngredientListViewModel(@NonNull Application application) {
        super(application);
        firestoreDatabase = FirestoreSource.getInstance();
        items = new MutableLiveData<>();
        foundItems = new MutableLiveData<>();
        itemsToShow = new MediatorLiveData<>();
    }

    protected void init(int[] suitableStageTypes) {
        this.suitableStageTypes = suitableStageTypes;
        getItemsFromFirestore();
        itemsToShow.addSource(items, ingredients -> itemsToShow.setValue(ingredients));
        itemsToShow.addSource(foundItems, ingredients -> itemsToShow.setValue(ingredients));
    }

    private void getItemsFromFirestore() {
        firestoreDatabase.ingredientDao.getIngredientsByStageTypes(suitableStageTypes).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {
                List<Ingredient> ingredients = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Ingredient ingredient = doc.toObject(Ingredient.class);
                    ingredients.add(ingredient);
                }
                items.postValue(ingredients);
            }
        });
    }

    public LiveData<List<Ingredient>> getItemsToShow() {
        return itemsToShow;
    }

    public void showAllItems() {
        itemsToShow.setValue(items.getValue());
    }

    public void searchByIngredientName(String ingredientName) {
        //       AsyncTask.execute(() -> {

        foundItems.postValue(findByName(ingredientName));
        //   });

    }

    public void deleteIngredient(String ingredientId) {
        if (items.getValue() != null) {
            Ingredient ingredient = findUsingIterator(ingredientId, items.getValue());
            if (ingredient != null) {
                //   AsyncTask.execute(() -> {
                firestoreDatabase.ingredientDao.delete(ingredient);
                //     });
            }
        }
    }

    private List<Ingredient> findByName(String search) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (items.getValue() != null)
            for (Ingredient ingredient : items.getValue()) {
                Log.d("aaa", "name = " + ingredient.name );
                if (ingredient.name.toLowerCase().contains(search.toLowerCase())) {
                    Log.d("aaa", "adding");
                    ingredients.add(ingredient);
                }
            }
        return ingredients;
    }

    protected Ingredient findUsingIterator(String id, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.id.equals(id)) {
                return ingredient;
            }
        }
        return null;
    }
}
