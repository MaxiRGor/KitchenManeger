package com.distinct.kitchenmanager.ui.shopping_list;

import android.app.Application;

import androidx.annotation.NonNull;

import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.ui.fragment_with_search_view.IngredientListViewModel;

public class ShoppingListViewModel extends IngredientListViewModel {


    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        init(new int[]{IngredientStageType.ToBuy.ordinal(), IngredientStageType.InBasket.ordinal()});
    }


    void changeIngredientState(String ingredientId, boolean isChecked) {
        if (items.getValue() != null) {
            Ingredient ingredient = findUsingIterator(ingredientId, items.getValue());
            if (ingredient != null) {
                if (isChecked && ingredient.stageType == IngredientStageType.ToBuy.ordinal()) {
                    ingredient.stageType = IngredientStageType.InBasket.ordinal();
                    //AsyncTask.execute(() ->
                    firestoreDatabase.ingredientDao.update(ingredient);
                    //);
                } else if (!isChecked && ingredient.stageType == IngredientStageType.InBasket.ordinal()) {
                    ingredient.stageType = IngredientStageType.ToBuy.ordinal();
                    // AsyncTask.execute(() ->
                    firestoreDatabase.ingredientDao.update(ingredient);
                    //);
                }
            }
        }
    }


}