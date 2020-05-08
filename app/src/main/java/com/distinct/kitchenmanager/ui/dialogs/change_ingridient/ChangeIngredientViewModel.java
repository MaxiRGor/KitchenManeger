package com.distinct.kitchenmanager.ui.dialogs.change_ingridient;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.model.room.database.RoomAppDatabase;
import com.distinct.kitchenmanager.model.room.database.RoomDatabaseSource;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ChangeIngredientViewModel extends ViewModel {

    int idOfIngredientToChange = -1;
    private RoomAppDatabase roomAppDatabase;
    private MutableLiveData<Ingredient> ingredientLiveData;
    private MutableLiveData<String[]> ingredientNamesLiveData;

    public ChangeIngredientViewModel() {
        roomAppDatabase = RoomDatabaseSource.getInstance(ApplicationContextSingleton.getInstance().getApplicationContext());
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
            List<Ingredient> ingredients = roomAppDatabase.ingredientDao().getAll();

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
            ingredient.amountOfDistinct = ingredientAmount;
            ingredient.weightType = weightType;
            ingredient.fullAmount = ingredientAmount * ingredient.amountOfIngredients;
            ingredient.stageType = IngredientStageType.ToBuy.ordinal();
            ingredient.comment = comment;
            ingredient.stageType = stageTypeOrdinal;
            ingredientLiveData.setValue(ingredient);
        }
    }

    void addIngredientToDatabase() {
        AsyncTask.execute(() -> roomAppDatabase.ingredientDao().insert(ingredientLiveData.getValue()));
    }

    void loadIngredientFromDatabase() {
        AsyncTask.execute(() -> {
            Ingredient ingredient = roomAppDatabase.ingredientDao().getById(idOfIngredientToChange);
            ingredientLiveData.postValue(ingredient);
        });
    }

    void setIngredientId() {
        Ingredient ingredient = ingredientLiveData.getValue();
        if (ingredient != null) {
            ingredient.id = idOfIngredientToChange;
            ingredientLiveData.setValue(ingredient);
        }
    }

    void updateIngredientInDatabase() {
        AsyncTask.execute(() -> roomAppDatabase.ingredientDao().update(ingredientLiveData.getValue()));
    }

    void setShelfLife(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Ingredient ingredient = ingredientLiveData.getValue();
        if (ingredient != null) {
            ingredient.shelfLifeTime = calendar.getTime().getTime();
            ingredientLiveData.setValue(ingredient);
        }
    }

    Calendar getShelfLifeDataCalendar() {
        Ingredient ingredient = ingredientLiveData.getValue();
        if (ingredient != null && ingredient.shelfLifeTime != 0) {
            Date date = new Date();
            date.setTime(ingredient.shelfLifeTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }
        return null;
    }
}
