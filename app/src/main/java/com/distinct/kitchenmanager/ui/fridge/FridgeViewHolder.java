package com.distinct.kitchenmanager.ui.fridge;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.element_behaviour.DateFormatter;
import com.distinct.kitchenmanager.model.room.entity.Ingredient;
import com.distinct.kitchenmanager.ui.dialogs.ChangeIngredientDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

public class FridgeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    FragmentManager fragmentManager;
    FridgeViewModel fridgeViewModel;
    public LinearLayout linearLayout;
    private Ingredient ingredient;
    private LinearLayout amountOfIngredientsLinearLayout;
    private LinearLayout ingredientAmountLinearLayout;
    private TextView amountOfIngredientsTextView;
    private TextView ingredientNameTextView;
    private TextView ingredientAmountTextView;
    private TextView ingredientWeightTypeTextView;
    private TextView manufacturerTextView;
    private TextView shelfLifeTextView;


    FridgeViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayout  = itemView.findViewById(R.id.item_fridge_linear_layout);
        amountOfIngredientsLinearLayout = itemView.findViewById(R.id.amount_of_ingredients_linear_layout);
        amountOfIngredientsTextView = itemView.findViewById(R.id.amount_of_ingredients_text_view);
        ingredientNameTextView = itemView.findViewById(R.id.ingredient_name_text_view);
        ingredientAmountTextView = itemView.findViewById(R.id.ingredient_amount_text_view);
        ingredientWeightTypeTextView = itemView.findViewById(R.id.weight_type_text_view);
        ingredientAmountLinearLayout = itemView.findViewById(R.id.ingredient_amount_linear_layout);
        manufacturerTextView = itemView.findViewById(R.id.manufacturer_text_view);
        shelfLifeTextView = itemView.findViewById(R.id.shelf_life_text_view);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DialogFragment dialog = ChangeIngredientDialogFragment.newInstance(ingredient.id);
        dialog.show(fragmentManager, "ChangeIngredientDialog");
    }


    @Override
    public boolean onLongClick(View view) {
        Snackbar.make(view, R.string.want_to_delete_item, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, v -> fridgeViewModel.deleteIngredient(ingredient.id))
                .show();
        return true;
    }

    void bind(Ingredient ingredient, String weightTypeString) {
        this.ingredient = ingredient;
        setName(ingredient);
        setManufacturer(ingredient);
        setShelfLife(ingredient);
        setAmountOfIngredients(ingredient);
        setAmount(ingredient, weightTypeString);
    }

    private void setName(Ingredient ingredient) {
        ingredientNameTextView.setText(ingredient.name);
    }

    private void setManufacturer(Ingredient ingredient) {
        if (!ingredient.manufacturer.equals(""))
            manufacturerTextView.setText(ingredient.manufacturer);
        else manufacturerTextView.setVisibility(View.GONE);
    }

    private void setShelfLife(Ingredient ingredient) {
        if (ingredient.shelfLifeTime != 0) {
            shelfLifeTextView.setText(DateFormatter.getStringFromDateTime(ingredient.shelfLifeTime));
        } else shelfLifeTextView.setVisibility(View.GONE);
    }

    private void setAmountOfIngredients(Ingredient ingredient) {
        if (ingredient.amountOfIngredients > 1) {
            amountOfIngredientsLinearLayout.setVisibility(View.VISIBLE);
            amountOfIngredientsTextView.setText(String.valueOf(ingredient.amountOfIngredients));
        } else {
            amountOfIngredientsLinearLayout.setVisibility(View.GONE);
        }
    }

    private void setAmount(Ingredient ingredient, String weightTypeString) {
        if (ingredient.amount != 0) {
            ingredientAmountLinearLayout.setVisibility(View.VISIBLE);
            String displayText;
            if (ingredient.amount % (int) ingredient.amount == 0) { // amount == integer
                if (ingredient.amountOfIngredients > 1) {
                    displayText = String.valueOf((int) ingredient.amount * ingredient.amountOfIngredients);
                } else
                    displayText = String.valueOf((int) ingredient.amount);
            } else { // amount == float
                if (ingredient.amountOfIngredients > 1) {
                    displayText = new DecimalFormat(".##").format(ingredient.amount * ingredient.amountOfIngredients);
                } else
                    displayText = new DecimalFormat(".##").format(ingredient.amount);
            }
            ingredientAmountTextView.setText(displayText);
            ingredientWeightTypeTextView.setText(weightTypeString);

        } else ingredientAmountLinearLayout.setVisibility(View.GONE);
    }


}