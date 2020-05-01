package com.distinct.kitchenmanager.ui.shopping_list;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.model.room.database.AppDatabase;
import com.distinct.kitchenmanager.model.room.database.DatabaseSource;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    private LiveData<List<Ingredient>> allShoppingListItems;
    private MutableLiveData<List<Ingredient>> foundShoppingListItems;
    private MediatorLiveData<List<Ingredient>> shoppingListItemsToShow;

    private int[] shoppingListStageTypes = new int[]{IngredientStageType.ToBuy.ordinal(), IngredientStageType.InBasket.ordinal()};

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = DatabaseSource.getInstance(application);
        allShoppingListItems = appDatabase.ingredientDao().getAllForShoppingListLiveData(shoppingListStageTypes);
        foundShoppingListItems = new MutableLiveData<>();
        shoppingListItemsToShow = new MediatorLiveData<>();
        shoppingListItemsToShow.addSource(allShoppingListItems, ingredients -> shoppingListItemsToShow.setValue(ingredients));
        shoppingListItemsToShow.addSource(foundShoppingListItems, ingredients -> shoppingListItemsToShow.setValue(ingredients));
    }


    LiveData<List<Ingredient>> getShoppingListItemsToShow() {
        return shoppingListItemsToShow;
    }

    void getAllShoppingListItems() {
        shoppingListItemsToShow.setValue(allShoppingListItems.getValue());
    }

    void searchByIngredientName(String ingredientName) {
        AsyncTask.execute(() ->
                foundShoppingListItems.postValue(appDatabase.ingredientDao().getByNameForShoppingList(shoppingListStageTypes, ingredientName))
        );
    }

    void changeIngredientState(int ingredientId, boolean isChecked) {
        if (allShoppingListItems.getValue() != null) {
            Ingredient ingredient = findUsingIterator(ingredientId, allShoppingListItems.getValue());
            if (ingredient != null) {
                if (isChecked && ingredient.stageType == IngredientStageType.ToBuy.ordinal()) {
                    ingredient.stageType = IngredientStageType.InBasket.ordinal();
                    AsyncTask.execute(() -> appDatabase.ingredientDao().update(ingredient));
                } else if (!isChecked && ingredient.stageType == IngredientStageType.InBasket.ordinal()) {
                    ingredient.stageType = IngredientStageType.ToBuy.ordinal();
                    AsyncTask.execute(() -> appDatabase.ingredientDao().update(ingredient));
                }
            }
        }
    }

    void deleteIngredient(int ingredientId) {
        if (allShoppingListItems.getValue() != null) {
            Ingredient ingredient = findUsingIterator(ingredientId, allShoppingListItems.getValue());
            if (ingredient != null) {
                AsyncTask.execute(() -> appDatabase.ingredientDao().delete(ingredient));
            }
        }
    }

    private Ingredient findUsingIterator(int id, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.id == id) {
                return ingredient;
            }
        }
        return null;
    }


}