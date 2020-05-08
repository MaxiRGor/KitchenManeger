package com.distinct.kitchenmanager.ui.food_dairy;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.model.room.entity.Consumed;

public class FoodDairyViewHolder extends RecyclerView.ViewHolder {

    private TextView consumedNameTextView;
    private TextView consumedCaloriesTextView;

    public FoodDairyViewHolder(@NonNull View itemView) {
        super(itemView);
        consumedNameTextView = itemView.findViewById(R.id.consumed_name_text_view);
        consumedCaloriesTextView = itemView.findViewById(R.id.consumed_calories_text_view);
    }

    void bind(Consumed consumed) {
        consumedNameTextView.setText(consumed.name);
        consumedCaloriesTextView.setText(String.format(ApplicationContextSingleton.getInstance().getApplicationContext().getString(R.string.calories), consumed.calories));
    }
}
