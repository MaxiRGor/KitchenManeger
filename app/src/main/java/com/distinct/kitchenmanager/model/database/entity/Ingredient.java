package com.distinct.kitchenmanager.model.database.entity;

public class Ingredient {


    public Ingredient() {
    }

    public Ingredient(int amountOfIngredients) {
        this.amountOfIngredients = amountOfIngredients;
    }

    public String id;

    public String name;

    public String manufacturer;

    public int weightType;

    public float amountOfDistinct;

    public int amountOfIngredients;

    public float fullAmount;

    public String category;

    public int caloriesInDistinct;

    public String comment;

    public int stageType;

    public long shelfLifeTime;

}
