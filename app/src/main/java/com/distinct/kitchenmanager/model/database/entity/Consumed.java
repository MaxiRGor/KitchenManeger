package com.distinct.kitchenmanager.model.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Consumed {
    public Consumed() {
    }

    @Ignore
    public Consumed(String name, int calories, String weight, long timeOfConsumption) {
        this.name = name;
        this.calories = calories;
        this.weight = weight;
        this.timeOfConsumption = timeOfConsumption;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "calories")
    public int calories;

    @ColumnInfo(name = "weight")
    public String weight;

    @ColumnInfo(name = "time_of_consumption")
    public long timeOfConsumption;


}
