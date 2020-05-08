package com.distinct.kitchenmanager.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.distinct.kitchenmanager.model.room.entity.Consumed;

import java.util.List;

@Dao
public interface ConsumedDao {
    @Query("SELECT * FROM Consumed")
    List<Consumed> getAll();

    @Query("SELECT * FROM Consumed where time_of_consumption between (:previousDayTime) and (:nextDayTime)")
    List<Consumed> getByDay(long previousDayTime, long nextDayTime);

    @Insert
    void insert(Consumed consumed);
}
