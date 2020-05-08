package com.distinct.kitchenmanager.ui.dialogs.consume_ingridient;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.model.room.database.RoomAppDatabase;
import com.distinct.kitchenmanager.model.room.database.RoomDatabaseSource;
import com.distinct.kitchenmanager.model.room.entity.Consumed;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;

import java.util.Date;

public class ConsumeIngredientViewModel extends ViewModel {

    public String weightType;
    private RoomAppDatabase roomAppDatabase;
    private MutableLiveData<Ingredient> ingredientLiveData;
    private MutableLiveData<Float> currentAmountToConsume;
    private String[] weightTypes;


    public ConsumeIngredientViewModel() {
        roomAppDatabase = RoomDatabaseSource.getInstance(ApplicationContextSingleton.getInstance().getApplicationContext());
        ingredientLiveData = new MutableLiveData<>();
        currentAmountToConsume = new MutableLiveData<>();
        weightType = "";
        ingredientLiveData.postValue(new Ingredient(1));

        AsyncTask.execute(() -> {
            Log.d("222", "size of consumed = " + roomAppDatabase.consumedDao().getAll().size());
        });
    }

    MutableLiveData<Ingredient> getIngredientLiveData() {
        return ingredientLiveData;
    }

    void loadIngredientFromDatabase(int idOfIngredient) {
        AsyncTask.execute(() -> {
            Ingredient ingredient = roomAppDatabase.ingredientDao().getById(idOfIngredient);
            ingredientLiveData.postValue(ingredient);
            currentAmountToConsume.postValue((float) ingredient.amountOfIngredients / 4);
            weightType = weightTypes[ingredient.weightType];
        });
    }

    MutableLiveData<Float> getCurrentAmountToConsume() {
        return currentAmountToConsume;
    }

    void setCurrentAmountToConsume(int progress) {
        if (ingredientLiveData.getValue() != null)
            currentAmountToConsume.setValue(progress * ingredientLiveData.getValue().fullAmount / 100);
    }

    void setWeightTypes(String[] weightTypes) {
        this.weightTypes = weightTypes;
    }

    void consumeIngredient() {
        if (currentAmountToConsume.getValue() != null && ingredientLiveData.getValue() != null) {
            float amountToConsume = currentAmountToConsume.getValue();
            if (amountToConsume == ingredientLiveData.getValue().fullAmount)
                fullyConsume(ingredientLiveData.getValue());
            else if (amountToConsume > 0)
                partlyConsume(amountToConsume, ingredientLiveData.getValue());
        }
    }


    private void fullyConsume(Ingredient ingredient) {
        int calories = (int) (ingredient.caloriesInDistinct * (ingredient.fullAmount / ingredient.amountOfDistinct));
        Consumed consumed = new Consumed(ingredient.name, calories, new Date().getTime());

        ingredient.stageType = IngredientStageType.Consumed.ordinal();

        AsyncTask.execute(() -> {
            roomAppDatabase.consumedDao().insert(consumed);
            roomAppDatabase.ingredientDao().update(ingredient);
        });
    }

    private void partlyConsume(float amountToConsume, Ingredient ingredient) {
        int calories = (int) (ingredient.caloriesInDistinct * (amountToConsume / ingredient.amountOfDistinct));
        Consumed consumed = new Consumed(ingredient.name, calories, new Date().getTime());
        ingredient.fullAmount -= amountToConsume;
        ingredient.amountOfIngredients = ((int) (ingredient.fullAmount / ingredient.amountOfDistinct) + 1);
        AsyncTask.execute(() -> {
            roomAppDatabase.consumedDao().insert(consumed);
            roomAppDatabase.ingredientDao().update(ingredient);
        });
    }

}
