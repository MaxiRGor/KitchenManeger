package com.distinct.kitchenmanager.model.database.entity;

//@Entity
public class Ingredient {

    public Ingredient() {
    }

    // @Ignore
    public Ingredient(int amountOfIngredients) {
        this.amountOfIngredients = amountOfIngredients;
    }

    //  @PrimaryKey(autoGenerate = true)
    public String id;

    //    @ColumnInfo(name = "name")
    public String name;

    //   @ColumnInfo(name = "manufacturer")
    public String manufacturer;

    //   @ColumnInfo(name = "weight_type")
    public int weightType;

    //   @ColumnInfo(name = "amount_of_distinct")
    public float amountOfDistinct;

    //   @ColumnInfo(name = "amount_of_items")
    public int amountOfIngredients;

    //  @ColumnInfo(name = "full_amount")
    public float fullAmount;

    //  @ColumnInfo(name = "calories_in_distinct")
    public int caloriesInDistinct;

    //   @ColumnInfo(name = "comment")
    public String comment;

    //   @ColumnInfo(name = "stage_type")
    public int stageType;

    //  @ColumnInfo(name = "shelf_life")
    public long shelfLifeTime;

}
