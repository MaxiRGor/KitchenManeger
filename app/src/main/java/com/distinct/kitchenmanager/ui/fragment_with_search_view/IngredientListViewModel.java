package com.distinct.kitchenmanager.ui.fragment_with_search_view;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.distinct.kitchenmanager.model.room.database.AppDatabase;
import com.distinct.kitchenmanager.model.room.database.DatabaseSource;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;

import java.util.List;

public class IngredientListViewModel extends AndroidViewModel {

    protected AppDatabase appDatabase;

    protected LiveData<List<Ingredient>> allItems;
    private MutableLiveData<List<Ingredient>> foundItems;
    private MediatorLiveData<List<Ingredient>> itemsToShow;
    private int[] suitableStageTypes;


    public IngredientListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = DatabaseSource.getInstance(application);
        foundItems = new MutableLiveData<>();
        itemsToShow = new MediatorLiveData<>();
    }

    protected void init(int[] suitableStageTypes) {
        this.suitableStageTypes = suitableStageTypes;
        allItems = appDatabase.ingredientDao().getAllByStageTypes(suitableStageTypes);
        itemsToShow.addSource(allItems, ingredients -> itemsToShow.setValue(ingredients));
        itemsToShow.addSource(foundItems, ingredients -> itemsToShow.setValue(ingredients));
    }

    public LiveData<List<Ingredient>> getItemsToShow() {
        return itemsToShow;
    }

    public void getAllItems() {
        itemsToShow.setValue(allItems.getValue());
    }

    public void searchByIngredientName(String ingredientName) {
        AsyncTask.execute(() ->
                foundItems.postValue(appDatabase.ingredientDao().getByStageTypesAndName(suitableStageTypes, ingredientName))
        );
    }

    public void deleteIngredient(int ingredientId) {
        if (allItems.getValue() != null) {
            Ingredient ingredient = findUsingIterator(ingredientId, allItems.getValue());
            if (ingredient != null) {
                AsyncTask.execute(() -> appDatabase.ingredientDao().delete(ingredient));
            }
        }
    }

    protected Ingredient findUsingIterator(int id, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.id == id) {
                return ingredient;
            }
        }
        return null;
    }
}
