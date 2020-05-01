package com.distinct.kitchenmanager.ui.fridge;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.distinct.kitchenmanager.ui.fragment_with_search_view.IngredientListViewModel;

import java.util.List;

public class FridgeViewModel extends IngredientListViewModel {


    public FridgeViewModel(@NonNull Application application) {
        super(application);
    }
}