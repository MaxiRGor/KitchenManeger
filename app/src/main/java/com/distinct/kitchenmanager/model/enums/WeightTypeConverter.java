package com.distinct.kitchenmanager.model.enums;

import androidx.room.TypeConverter;

import com.distinct.kitchenmanager.MainActivity;
import com.distinct.kitchenmanager.R;

public class WeightTypeConverter {
    @TypeConverter
    public int fromWeightType(WeightType weightType) {
        return weightType.ordinal();
    }

/*    @TypeConverter
    public WeightType toWeightType(String name) {

        if (name.equals(MainActivity.getContext().getResources().getString(R.string.weight_type_grams)))
            return WeightType.Grams;
        else if (name.equals(MainActivity.getContext().getResources().getString(R.string.weight_type_litres)))
            return WeightType.Litres;
        else return WeightType.Pieces;
    }*/
}
