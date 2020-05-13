package com.distinct.kitchenmanager.ui.dialogs.consume_ingridient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.IngredientAmountFormatter;
import com.distinct.kitchenmanager.model.database.database.FirestoreDatabase;
import com.distinct.kitchenmanager.model.database.database.FirestoreSource;
import com.distinct.kitchenmanager.model.database.database.RoomAppDatabase;
import com.distinct.kitchenmanager.model.database.database.RoomDatabaseSource;
import com.distinct.kitchenmanager.model.database.entity.Consumed;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;

import java.util.Date;

public class ConsumeIngredientViewModel extends ViewModel {

    public String weightType;
    private RoomAppDatabase roomAppDatabase;
    private FirestoreDatabase firestoreDatabase;
    private MutableLiveData<Ingredient> ingredientLiveData;
    private MutableLiveData<Float> currentAmountToConsume;
    private String[] weightTypes;


    public ConsumeIngredientViewModel() {
        roomAppDatabase = RoomDatabaseSource.getInstance(ApplicationContextSingleton.getInstance().getApplicationContext());
        firestoreDatabase = FirestoreSource.getInstance();
        ingredientLiveData = new MutableLiveData<>();
        currentAmountToConsume = new MutableLiveData<>();
        weightType = "";
        ingredientLiveData.postValue(new Ingredient(1));
    }

    MutableLiveData<Ingredient> getIngredientLiveData() {
        return ingredientLiveData;
    }

    void loadIngredientFromDatabase(String idOfIngredient) {
        firestoreDatabase.ingredientDao.getById(idOfIngredient).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                Ingredient ingredient = documentSnapshot.toObject(Ingredient.class);
                ingredientLiveData.postValue(ingredient);
                if (ingredient != null) {
                    currentAmountToConsume.postValue((float) ingredient.amountOfIngredients / 4);
                    weightType = weightTypes[ingredient.weightType];
                }
            }
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

    void consumeIngredient(Context context) {
        if (currentAmountToConsume.getValue() != null && ingredientLiveData.getValue() != null) {
            float amountToConsume = currentAmountToConsume.getValue();
            String consumedWeight = IngredientAmountFormatter.geFormattedString(currentAmountToConsume.getValue()) + " " + weightType;
            if (amountToConsume == ingredientLiveData.getValue().fullAmount)
                fullyConsume(ingredientLiveData.getValue(), consumedWeight, context);
            else if (amountToConsume > 0)
                partlyConsume(amountToConsume, ingredientLiveData.getValue(), consumedWeight, context);
        }
    }


    private void fullyConsume(Ingredient ingredient, String consumedWeight, Context context) {

        int calories = (int) (ingredient.caloriesInDistinct * (ingredient.fullAmount / ingredient.amountOfDistinct));
        showToast(context, calories);
        Consumed consumed = new Consumed(ingredient.name, calories, consumedWeight, new Date().getTime());
        ingredient.stageType = IngredientStageType.Consumed.ordinal();

        AsyncTask.execute(() -> {
            roomAppDatabase.consumedDao().insert(consumed);
            firestoreDatabase.ingredientDao.update(ingredient);
        });
    }

    private void partlyConsume(float amountToConsume, Ingredient ingredient, String consumedWeight, Context context) {
        int calories = (int) (ingredient.caloriesInDistinct * (amountToConsume / ingredient.amountOfDistinct));
        showToast(context, calories);
        Consumed consumed = new Consumed(ingredient.name, calories, consumedWeight, new Date().getTime());
        ingredient.fullAmount -= amountToConsume;
        ingredient.amountOfIngredients = ((int) (ingredient.fullAmount / ingredient.amountOfDistinct) + 1);
        AsyncTask.execute(() -> {
            roomAppDatabase.consumedDao().insert(consumed);
            firestoreDatabase.ingredientDao.update(ingredient);

        });
    }

    private void showToast(Context context, int calories) {
        Toast.makeText(context, String.format(ApplicationContextSingleton.getInstance().getApplicationContext().getString(R.string.consumed_x_calories), calories), Toast.LENGTH_LONG).show();
    }

    void incrementConsumedWeight() {

        if (currentAmountToConsume.getValue() != null && ingredientLiveData.getValue() != null && currentAmountToConsume.getValue() < ingredientLiveData.getValue().fullAmount)
            currentAmountToConsume.setValue((float) Math.ceil(currentAmountToConsume.getValue() + 0.001));
    }

    void decrementConsumedWeight() {
        if (currentAmountToConsume.getValue() != null && currentAmountToConsume.getValue() > 0) {
            currentAmountToConsume.setValue((float) Math.floor(currentAmountToConsume.getValue() - 0.001));
        }
    }

}
