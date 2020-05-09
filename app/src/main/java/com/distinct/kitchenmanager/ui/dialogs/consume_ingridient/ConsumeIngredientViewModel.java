package com.distinct.kitchenmanager.ui.dialogs.consume_ingridient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.R;
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

        AsyncTask.execute(() -> {
            Log.d("222", "size of consumed = " + roomAppDatabase.consumedDao().getAll().size());
        });
    }

    MutableLiveData<Ingredient> getIngredientLiveData() {
        return ingredientLiveData;
    }

    void loadIngredientFromDatabase(String idOfIngredient) {
        //     AsyncTask.execute(() -> {
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

        //   });
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
            if (amountToConsume == ingredientLiveData.getValue().fullAmount)
                fullyConsume(ingredientLiveData.getValue(), context);
            else if (amountToConsume > 0)
                partlyConsume(amountToConsume, ingredientLiveData.getValue(), context);
        }
    }


    private void fullyConsume(Ingredient ingredient, Context context) {
        Log.d("aaa", "ingredient.caloriesInDistinct = " + ingredient.caloriesInDistinct);
        Log.d("aaa", "ingredient.fullAmount = " + ingredient.fullAmount);
        Log.d("aaa", " ingredient.amountOfDistinct = " + ingredient.amountOfDistinct);
        int calories = (int) (ingredient.caloriesInDistinct * (ingredient.fullAmount / ingredient.amountOfDistinct));
        showToast(context, calories);
        Consumed consumed = new Consumed(ingredient.name, calories, new Date().getTime());
        ingredient.stageType = IngredientStageType.Consumed.ordinal();
        AsyncTask.execute(() -> {
            roomAppDatabase.consumedDao().insert(consumed);
            firestoreDatabase.ingredientDao.update(ingredient);

        });
    }

    private void partlyConsume(float amountToConsume, Ingredient ingredient, Context context) {
        int calories = (int) (ingredient.caloriesInDistinct * (amountToConsume / ingredient.amountOfDistinct));
        showToast(context, calories);
        Consumed consumed = new Consumed(ingredient.name, calories, new Date().getTime());
        ingredient.fullAmount -= amountToConsume;
        ingredient.amountOfIngredients = ((int) (ingredient.fullAmount / ingredient.amountOfDistinct) + 1);
        AsyncTask.execute(() -> {
            roomAppDatabase.consumedDao().insert(consumed);
            firestoreDatabase.ingredientDao.update(ingredient);

        });
    }

    private void showToast(Context context, int calories) {
        Log.d("aaa", "showing toast");
        Toast.makeText(context, String.format(ApplicationContextSingleton.getInstance().getApplicationContext().getString(R.string.consumed_x_calories), calories), Toast.LENGTH_LONG).show();
    }

}
