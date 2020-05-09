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
import com.distinct.kitchenmanager.element_behaviour.FullAmountFormatter;
import com.distinct.kitchenmanager.model.database.entity.Ingredient;
import com.distinct.kitchenmanager.ui.dialogs.change_ingridient.ChangeIngredientDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class FridgeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    FragmentManager fragmentManager;
    FridgeViewModel fridgeViewModel;
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
        if (ingredient.amountOfDistinct != 0) {
            ingredientAmountLinearLayout.setVisibility(View.VISIBLE);
            ingredientAmountTextView.setText(FullAmountFormatter.geFormattedString(ingredient.fullAmount));
            ingredientWeightTypeTextView.setText(weightTypeString);

        } else ingredientAmountLinearLayout.setVisibility(View.GONE);
    }

}