package com.distinct.kitchenmanager.model.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Ingredient {

    public Ingredient(){}

    @Ignore
    public Ingredient(int amountOfIngredients){
        this.amountOfIngredients = amountOfIngredients;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "manufacturer")
    public String manufacturer;

    @ColumnInfo(name = "weight_type")
    public int weightType;

    @ColumnInfo(name = "amount")
    public float amount;

    @ColumnInfo(name = "amount_of_items")
    public int amountOfIngredients;

    @ColumnInfo(name = "comment")
    public String comment;

    @ColumnInfo(name = "stage_type")
    public int stageType;

    @ColumnInfo(name = "shelf_life")
    public long shelfLifeDate;

}