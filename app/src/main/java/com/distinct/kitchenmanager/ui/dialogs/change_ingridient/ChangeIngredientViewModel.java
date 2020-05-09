package com.distinct.kitchenmanager.ui.dialogs.change_ingridient;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.distinct.kitchenmanager.model.database.database.FirestoreDatabase;
import com.distinct.kitchenmanager.model.database.database.FirestoreSource;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ChangeIngredientViewModel extends ViewModel {

    String idOfIngredientToChange = "";
    private FirestoreDatabase firestoreDatabase;
    private MutableLiveData<Ingredient> ingredientLiveData;
    private MutableLiveData<String[]> ingredientNamesLiveData;

    public ChangeIngredientViewModel() {
        firestoreDatabase = FirestoreSource.getInstance();
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
        //  AsyncTask.execute(() -> {
        firestoreDatabase.ingredientDao.getAll().addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {


                List<Ingredient> ingredients = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Ingredient ingredient = doc.toObject(Ingredient.class);
                    ingredients.add(ingredient);
                }

                HashSet<String> names = new HashSet<>();
                for (Ingredient ingredient : ingredients) {
                    names.add(ingredient.name);
                }
                String[] namesArray = new String[names.size()];
                names.toArray(namesArray);
                ingredientNamesLiveData.postValue(namesArray);
            }
        });


        //    });

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


    void setValues(String ingredientName, String manufacturerName, float ingredientAmount,
                   int weightType, String comment, int stageTypeOrdinal, int calories) {
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
            ingredient.caloriesInDistinct = calories;
            ingredientLiveData.setValue(ingredient);
        }
    }

    void addIngredientToDatabase() {
        // AsyncTask.execute(() ->
        firestoreDatabase.ingredientDao.insert(ingredientLiveData.getValue());
        //);
    }

    void loadIngredientFromDatabase() {
        // AsyncTask.execute(() -> {
        firestoreDatabase.ingredientDao.getById(idOfIngredientToChange).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                ingredientLiveData.postValue(documentSnapshot.toObject(Ingredient.class));
            }
        });

        //    });
    }

    void setIngredientId() {
        Ingredient ingredient = ingredientLiveData.getValue();
        if (ingredient != null) {
            ingredient.id = idOfIngredientToChange;
            ingredientLiveData.setValue(ingredient);
        }
    }

    void updateIngredientInDatabase() {
        //  AsyncTask.execute(() -> {
        if (ingredientLiveData.getValue() != null)
            firestoreDatabase.ingredientDao.update(ingredientLiveData.getValue());
        //  });
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
