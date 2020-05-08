package com.distinct.kitchenmanager.ui.food_dairy;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.model.room.database.RoomAppDatabase;
import com.distinct.kitchenmanager.model.room.database.RoomDatabaseSource;
import com.distinct.kitchenmanager.model.room.entity.Consumed;

import java.util.Calendar;
import java.util.List;

public class FoodDairyViewModel extends ViewModel {

    private MutableLiveData<List<Consumed>> consumedItems;
    private RoomAppDatabase roomAppDatabase;

    public FoodDairyViewModel() {
        consumedItems = new MutableLiveData<>();
        roomAppDatabase = RoomDatabaseSource.getInstance(ApplicationContextSingleton.getInstance().getApplicationContext());
        loadItemsByDate(Calendar.getInstance());
    }

    LiveData<List<Consumed>> getConsumedItems() {
        return consumedItems;
    }

    void setPickedDate(Calendar calendar) {
        loadItemsByDate(calendar);
    }

    private void loadItemsByDate(Calendar currentDay) {
        AsyncTask.execute(() -> {
            consumedItems.postValue(roomAppDatabase.consumedDao().getByDay(getPrevDayTime(currentDay), getNextDayTime(currentDay)));
        });
    }

    private long getNextDayTime(Calendar currentDay) {
        currentDay.add(Calendar.DAY_OF_MONTH, +1);
        return currentDay.getTime().getTime();
    }

    private long getPrevDayTime(Calendar currentDay) {
        currentDay.add(Calendar.DAY_OF_MONTH, -1);
        return currentDay.getTime().getTime();
    }

}