package com.distinct.kitchenmanager.ui.fridge;

import android.app.Application;

import androidx.annotation.NonNull;

import com.distinct.kitchenmanager.model.enums.IngredientStageType;
import com.distinct.kitchenmanager.ui.fragment_with_search_view.IngredientListViewModel;

public class FridgeViewModel extends IngredientListViewModel {


    public FridgeViewModel(@NonNull Application application) {
        super(application);
        init(new int[]{IngredientStageType.InFridge.ordinal()});
    }
}