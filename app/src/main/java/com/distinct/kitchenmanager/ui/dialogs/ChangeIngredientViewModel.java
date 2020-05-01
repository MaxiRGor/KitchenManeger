package com.distinct.kitchenmanager.ui.dialogs;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.model.room.database.AppDatabase;
import com.distinct.kitchenmanager.model.room.database.DatabaseSource;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;

import java.util.HashSet;
import java.util.List;

public class ChangeIngredientViewModel extends ViewModel {

    int idOfIngredientToChange = -1;
    private AppDatabase appDatabase;
    private MutableLiveData<Ingredient> ingredientLiveData;
    private MutableLiveData<String[]> ingredientNamesLiveData;

    public ChangeIngredientViewModel() {
        appDatabase = DatabaseSource.getInstance(ApplicationContextSingleton.getInstance().getApplicationContext());
        ingredientLiveData = new MutableLiveData<>();
        ingredientLiveData.postValue(new Ingredient(1));

        ingredientNamesLiveData = new MutableLiveData<>();
        getIngredientNamesFromDatabase();
    }

    MutableLiveData<Ingredient> getIngredientLiveData() {
        return ingredientLiveData;
    }


    MutableLiveData<String[]> getIngredientNamesLiveData() {
        return ingredientNamesLiveData;
    }

    private void getIngredientNamesFromDatabase() {
        AsyncTask.execute(() -> {
            HashSet<String> names = new HashSet<>();
            List<Ingredient> ingredients = appDatabase.ingredientDao().getAll();

            for (Ingredient ingredient : ingredients) {
                names.add(ingredient.name);
            }

            String[] namesArray = new String[names.size()];
            names.toArray(namesArray);
            ingredientNamesLiveData.postValue(namesArray);

        });

    }


    void incrementAmountOfIngredients() {
        Ingredient ingredient = ingredientLiveData.getValue();
        if (ingredient != null) {
            ingredient.amountOfIngredients++;
            ingredientLiveData.setValue(ingredient);
        }
    }

    void decrementAmountOfIngredients() {
        Ingredient ingredient = ingredientLiveData.getValue();
        if (ingredient != null && ingredient.amountOfIngredients > 1) {
            ingredient.amountOfIngredients--;
            ingredientLiveData.setValue(ingredient);
        }
    }


    void setValues(String ingredientName, String manufacturerName, float ingredientAmount, int weightType, String comment, int stageTypeOrdinal) {
        Ingredient ingredient = ingredientLiveData.getValue();
        if (ingredient != null && ingredient.amountOfIngredients > 0) {
            ingredient.name = ingredientName;
            ingredient.manufacturer = manufacturerName;
            ingredient.amount = ingredientAmount;
            ingredient.weightType = weightType;
            ingredient.stageType = IngredientStageType.ToBuy.ordinal();
            ingredient.comment = comment;
            ingredient.stageType = stageTypeOrdinal;

            ingredientLiveData.setValue(ingredient);
        }
    }

    void addIngredientToDatabase() {
        AsyncTask.execute(() -> appDatabase.ingredientDao().insert(ingredientLiveData.getValue()));
    }

    void loadIngredientFromDatabase() {
        AsyncTask.execute(() -> {
            Ingredient ingredient = appDatabase.ingredientDao().getById(idOfIngredientToChange);
            ingredientLiveData.postValue(ingredient);
        });
    }

    void setIngredientId() {
        Ingredient ingredient = ingredientLiveData.getValue();
        if (ingredient != null && ingredient.amountOfIngredients > 0) {
            ingredient.id = idOfIngredientToChange;
            ingredientLiveData.setValue(ingredient);
        }
    }

    void updateIngredientInDatabase() {
        AsyncTask.execute(() -> appDatabase.ingredientDao().update(ingredientLiveData.getValue()));
    }
}
