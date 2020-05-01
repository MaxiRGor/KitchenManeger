package com.distinct.kitchenmanager.ui.food_dairy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodDairyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FoodDairyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}